import React from "react";
import { render, screen } from "@testing-library/react";
import { Typography } from "@mui/material";
import { DateTimeColumn } from "./DateTimeColumn"; // adjust the path accordingly
import { GridCellParams } from "@mui/x-data-grid";

describe("DateTimeColumn", () => {
    const getDateValue = (row: any) => row.dateString;
    const header = "testDate";

    it("returns a column definition with correct properties", () => {
        const colDef = DateTimeColumn(getDateValue, "right", header);

        expect(colDef.field).toBe(header);
        expect(colDef.headerAlign).toBe("right");
    });

    it("renders the cell with formatted date", () => {
        const colDef = DateTimeColumn(getDateValue, "center", header);

        const mockRow = { dateString: "2025-07-24T12:34:56Z" };
        const params = { row: mockRow } as GridCellParams;

        const { container } = render(<>{colDef.renderCell?.(params)}</>);
        expect(container.textContent).toBe("24/07/25 12:34");
    });
});
