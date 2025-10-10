import { render, screen, fireEvent } from "@testing-library/react";
import { GridCellParams } from "@mui/x-data-grid";
import CreateChallengesColumns from "../CreateChallengesColumns";

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

jest.mock('@/shared/components/Common/CustomButton', () => (props: any) => (
    <button data-testid="custom-button" onClick={props.onClick} style={props.style}>
        {props.children}
    </button>
));

describe("CreateChallengesColumns", () => {
    const handleChallengeButtonClick = jest.fn();

    const columns = CreateChallengesColumns(handleChallengeButtonClick);

    it("returns two columns with expected fields and headers", () => {
        expect(columns).toHaveLength(2);

        // TeamNameColumn header
        const { unmount: unmountTeamNameHeader } = render(<>{columns[0].renderHeader?.()}</>);
        expect(screen.getByTestId("teamname-header-Team Name")).toHaveTextContent("Team Name");
        unmountTeamNameHeader();

        // Action header
        const { unmount: unmountActionHeader } = render(<>{columns[1].renderHeader?.()}</>);
        expect(screen.getByTestId("col-header")).toHaveTextContent("Action");
        unmountActionHeader();
    });

    it("renders team name cell properly", () => {
        const teamRow = { id: "team123", name: "Awesome Team" };
        const params = { row: teamRow } as GridCellParams;

        const cell = columns[0].renderCell(params);
        const { getByTestId } = render(<>{cell}</>);
        expect(getByTestId("teamname-cell-Team Name")).toHaveTextContent("Awesome Team");
    });

    it("renders action cell with challenge button and calls handler on click", () => {
        const params = { row: { id: "challengeId" } } as GridCellParams;

        const cell = columns[1].renderCell(params);
        const { getByTestId } = render(<>{cell}</>);
        const button = getByTestId("custom-button");
        expect(button).toHaveTextContent("Challenge");

        fireEvent.click(button);
        expect(handleChallengeButtonClick).toHaveBeenCalledWith("challengeId");
    });
});
