import React from "react";
import { render, screen } from "@testing-library/react";
import TeamDataGrid from "../TeamDatagrid";
import { mockPlayers } from "__mocks__/player";

describe("TeamDataGrid", () => {

    it.skip("applies 'super-app-theme--selected' class when activePlayer is selected", () => {
        render(
            <TeamDataGrid
                rows={mockPlayers}
                onSelectChange={jest.fn()}
                activePlayer={mockPlayers[0]}
            />
        );

        const selectedRow = screen.getByText("Willy Treutel").closest(".MuiDataGrid-row");

        expect(selectedRow).toHaveClass("super-app-theme--selected");
    });

    it.skip("applies 'super-app-theme--notselected' class to non-active players", () => {
        render(
            <TeamDataGrid
                rows={mockPlayers}
                onSelectChange={jest.fn()}
                activePlayer={mockPlayers[0]}
            />
        );

        const notSelectedRow = screen.getByText("W. Two").closest(".MuiDataGrid-row");

        expect(notSelectedRow).toHaveClass("super-app-theme--notselected");
    });
});
