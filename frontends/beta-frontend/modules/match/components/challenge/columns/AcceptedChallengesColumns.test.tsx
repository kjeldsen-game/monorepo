import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import { GridCellParams } from "@mui/x-data-grid";
import AcceptedChallengesColumns from "./AcceptedChallengesColumns";

jest.mock('@/shared/components/Grid/Columns/common/components/ColHeader', () => (props: any) => (
    <div data-testid="col-header">{props.header}</div>
));

jest.mock('../../columns/TeamNameColumn', () => ({
    TeamNameColumn: (getTeamNameValue: any, alignment: any, header: any) => ({
        field: header,
        renderHeader: () => <div data-testid={`teamname-header-${header}`}>{header}</div>,
        renderCell: (params: GridCellParams) => {
            const team = getTeamNameValue(params.row);
            return <div data-testid={`teamname-cell-${header}`}>{team.name}</div>;
        },
    }),
}));

jest.mock('@/shared/components/Grid/Columns/common/config/ColumnsConfig', () => ({
    getColumnConfig: (alignment?: string) => ({
        sortable: true,
        headerAlign: alignment || 'center',
    }),
}));

jest.mock('@/shared/components/Common/CustomIconButton', () => (props: any) => (
    <button data-testid="custom-icon-button" onClick={props.onClick}>{props.children}</button>
));

jest.mock('@/shared/components/Common/LinkButton', () => (props: any) => (
    <a data-testid="link-button" href={props.link}>
        {props.children}
    </a>
));

jest.mock('@mui/icons-material/PlayArrow', () => () => <span data-testid="play-arrow-icon" />);
jest.mock('@mui/x-date-pickers', () => ({
    ClearIcon: () => <span data-testid="clear-icon" />,
}));

describe("AcceptedChallengesColumns", () => {
    const teamId = "team1";

    const handlePlayButtonClick = jest.fn();
    const handleCancelButtonBlick = jest.fn();

    const columns = AcceptedChallengesColumns(teamId, handlePlayButtonClick, handleCancelButtonBlick);

    it("returns an array of columns with correct fields and headers", () => {
        expect(columns).toHaveLength(4);

        // TeamNameColumn header mocks
        expect(columns[0].field).toBe("Home");
        expect(columns[1].field).toBe("Away");

        // Verify headers render correctly
        const { container: containerHome } = render(<>{columns[0].renderHeader?.()}</>);
        expect(screen.getByTestId("teamname-header-Home")).toHaveTextContent("Home");

        const { container: containerAway } = render(<>{columns[1].renderHeader?.()}</>);
        expect(screen.getByTestId("teamname-header-Away")).toHaveTextContent("Away");

        // Lineup header
        const { container: containerLineup } = render(<>{columns[2].renderHeader?.()}</>);
        const colHeaders = screen.getAllByTestId("col-header");
        expect(colHeaders.some(el => el.textContent === "Lineup")).toBe(true);

        // Actions header
        const { container: containerActions } = render(<>{columns[3].renderHeader?.()}</>);
        const colHeadersAfter = screen.getAllByTestId("col-header");
        expect(colHeadersAfter.some(el => el.textContent === "Action")).toBe(true);

    });

    describe("lineup column renderCell", () => {
        const lineupCol = columns[2];

        it("renders 'Custom' when the team matches teamId and has specificLineup", () => {
            const params = {
                row: {
                    home: { id: teamId, specificLineup: true },
                    away: { id: "otherTeam" },
                },
            } as GridCellParams;

            const { container } = render(<>{lineupCol.renderCell(params)}</>);
            expect(container.textContent).toBe("Custom");
        });

        it("renders 'Default' when the team matches teamId and has no specificLineup", () => {
            const params = {
                row: {
                    home: { id: teamId, specificLineup: false },
                    away: { id: "otherTeam" },
                },
            } as GridCellParams;

            const { container } = render(<>{lineupCol.renderCell(params)}</>);
            expect(container.textContent).toBe("Default");
        });

        it("renders null when no team matches the teamId", () => {
            const params = {
                row: {
                    home: { id: "other1" },
                    away: { id: "other2" },
                },
            } as GridCellParams;

            const { container } = render(<>{lineupCol.renderCell(params)}</>);
            expect(container.textContent).toBe("");
        });
    });

    describe("actions column renderCell", () => {
        const actionsCol = columns[3];

        it("renders PlayArrow button if status ACCEPTED and home team matches teamId", () => {
            const params = {
                row: {
                    id: "match1",
                    status: "ACCEPTED",
                    home: { id: teamId },
                    away: { name: "AwayTeam" },
                },
            } as GridCellParams;

            render(<>{actionsCol.renderCell(params)}</>);

            // Check PlayArrow button is rendered
            const playBtn = screen.getByTestId("custom-icon-button");
            expect(playBtn).toBeInTheDocument();

            // Click the play button
            fireEvent.click(playBtn);
            expect(handlePlayButtonClick).toHaveBeenCalledWith("match1");
        });

        it("renders Cancel button and Set Lineup link for self challenge", () => {
            const params = {
                row: {
                    id: "match2",
                    status: "ACCEPTED",
                    home: { id: "teamX", name: "SameName" },
                    away: { name: "SameName" },
                },
            } as GridCellParams;

            render(<>{actionsCol.renderCell(params)}</>);

            // Cancel button
            const cancelBtn = screen.getAllByTestId("custom-icon-button")[0];
            expect(cancelBtn).toBeInTheDocument();

            fireEvent.click(cancelBtn);
            expect(handleCancelButtonBlick).toHaveBeenCalledWith("match2");

            // Set Lineup link for self challenge
            const link = screen.getByTestId("link-button");
            expect(link).toHaveAttribute("href", "/match/self/match2");
            expect(link).toHaveTextContent("Set Lineup");
        });

        it("renders Set Lineup link for normal challenge", () => {
            const params = {
                row: {
                    id: "match3",
                    status: "ACCEPTED",
                    home: { id: "teamX", name: "HomeTeam" },
                    away: { name: "AwayTeam" },
                },
            } as GridCellParams;

            render(<>{actionsCol.renderCell(params)}</>);

            const links = screen.getAllByTestId("link-button");
            // Should be one link only
            expect(links).toHaveLength(1);
            expect(links[0]).toHaveAttribute("href", "/match/lineup/match3");
            expect(links[0]).toHaveTextContent("Set Lineup");
        });
    });
});
