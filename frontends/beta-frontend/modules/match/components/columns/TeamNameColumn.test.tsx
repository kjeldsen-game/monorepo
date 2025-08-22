import React from "react";
import { render, screen } from "@testing-library/react";
import { TeamNameColumn } from "./TeamNameColumn"; // Adjust path accordingly
import { GridCellParams } from "@mui/x-data-grid";

jest.mock("@/shared/components/Grid/Columns/common/config/ColumnsConfig", () => ({
    getColumnConfig: (alignment: any) => ({ sortable: true, headerAlign: alignment }),
}));

jest.mock("@/shared/components/Grid/Columns/common/components/ColHeader", () => (props: any) => {
    return <div data-testid="col-header">{props.header}</div>;
});

jest.mock("@/shared/components/Grid/Columns/common/components/ColLink", () => (props: any) => {
    return (
        <a
            data-testid="col-link"
            href={props.urlValue}
            style={props.sx}
        >
            {props.children}
        </a>
    );
});

describe("TeamNameColumn", () => {
    const getTeamNameValue = (row: any) => row.team;
    const header = "teamName";

    it("returns correct column config", () => {
        const colDef = TeamNameColumn(getTeamNameValue, "right", header);

        expect(colDef.field).toBe(header);
        expect(colDef.headerAlign).toBe("right");
        expect(colDef.sortable).toBe(true);
    });

    it("renders the cell with team name and correct URL and padding (left aligned)", () => {
        const colDef = TeamNameColumn(getTeamNameValue, "left", header);

        const mockRow = { team: { id: 123, name: "Team Alpha" } };
        const params = { row: mockRow } as GridCellParams;

        render(<>{colDef.renderCell?.(params)}</>);
        const linkEl = screen.getByTestId("col-link");

        expect(linkEl).toHaveTextContent("Team Alpha");
        expect(linkEl).toHaveAttribute("href", "/team/123");
        expect(linkEl).toHaveStyle({ paddingLeft: "10px" });
    });

    it("renders the cell with team name and correct URL and padding (right aligned)", () => {
        const colDef = TeamNameColumn(getTeamNameValue, "right", header);

        const mockRow = { team: { id: 456, name: "Team Beta" } };
        const params = { row: mockRow } as GridCellParams;

        render(<>{colDef.renderCell?.(params)}</>);
        const linkEl = screen.getByTestId("col-link");

        expect(linkEl).toHaveTextContent("Team Beta");
        expect(linkEl).toHaveAttribute("href", "/team/456");
        expect(linkEl).toHaveStyle({ paddingRight: "10px" });
    });

    it("renders the cell with team name and correct URL and no padding (center aligned)", () => {
        const colDef = TeamNameColumn(getTeamNameValue, "center", header);

        const mockRow = { team: { id: 789, name: "Team Gamma" } };
        const params = { row: mockRow } as GridCellParams;

        render(<>{colDef.renderCell?.(params)}</>);
        const linkEl = screen.getByTestId("col-link");

        expect(linkEl).toHaveTextContent("Team Gamma");
        expect(linkEl).toHaveAttribute("href", "/team/789");
        expect(linkEl).not.toHaveStyle("paddingLeft: 10px");
        expect(linkEl).not.toHaveStyle("paddingRight: 10px");
    });
});
