import { render, screen } from "@testing-library/react";
import { DataGrid } from "@mui/x-data-grid";
import { LineupColumns } from "../LineupColumns";

describe("LineupColumns - headers", () => {
    const renderGrid = (isXs = false, isXsPlayers = false) => {
        const columns = LineupColumns(
            false,
            jest.fn(),
            undefined,
            jest.fn(),
            isXs,
            isXsPlayers
        );

        render(
            <div>
                <DataGrid
                    rows={[]}
                    columns={columns}
                    getRowId={(row) => row.id}
                />
            </div>
        );
    };

    it("renders core headers (Name, Pos, Act. Pos, Action)", () => {
        renderGrid();
        expect(screen.getByText(/Name/i)).toBeInTheDocument();
        expect(screen.getByText("Pos")).toBeInTheDocument();
        expect(screen.getByText("Act. Pos")).toBeInTheDocument();
        expect(screen.getByText("Action")).toBeInTheDocument();
    });

    it("renders skill headers when not xs", () => {
        renderGrid(false, false);
        expect(screen.getByText(/SC/i)).toBeInTheDocument();
    });

    it("hides Action header on xs with xsPlayers", () => {
        renderGrid(true, true);
        expect(screen.queryByText("Action")).not.toBeInTheDocument();
    });
});
