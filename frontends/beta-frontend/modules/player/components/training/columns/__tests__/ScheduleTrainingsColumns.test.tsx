import { render, screen } from "@testing-library/react";
import { ScheduleTrainingsColumns } from "../ScheduleTrainingsColumns";

describe("ScheduleTrainingsColumns", () => {
    const mockHandleCellClick = jest.fn();

    const columns = ScheduleTrainingsColumns(mockHandleCellClick, false);


    it("renders correct headers on non xs screen", () => {
        columns.map((col) =>
            render(<>{col.renderHeader?.({} as any)}</>)
        );
        expect(screen.getByText("Name")).toBeInTheDocument();
        expect(screen.getByText("Pos")).toBeInTheDocument();
        expect(screen.getByText("CS")).toBeInTheDocument();
        expect(screen.getByText("SC")).toBeInTheDocument();
        expect(screen.getByText("OP")).toBeInTheDocument();
        expect(screen.getByText("BC")).toBeInTheDocument();
        expect(screen.getByText("PA")).toBeInTheDocument();
        expect(screen.getByText("AE")).toBeInTheDocument();
        expect(screen.getByText("CO")).toBeInTheDocument();
        expect(screen.getByText("TA")).toBeInTheDocument();
        expect(screen.getByText("DP")).toBeInTheDocument();
    });

    it("renders correct headers on xs screen", () => {
        const columns = ScheduleTrainingsColumns(mockHandleCellClick, true);
        columns.map((col) =>
            render(<>{col.renderHeader?.({} as any)}</>)
        );
        expect(screen.getByText("Name")).toBeInTheDocument();
        expect(screen.getByText("Pos")).toBeInTheDocument();
        expect(screen.queryByText("CS")).not.toBeInTheDocument();
        expect(screen.getByText("SC")).toBeInTheDocument();
        expect(screen.getByText("OP")).toBeInTheDocument();
        expect(screen.getByText("BC")).toBeInTheDocument();
        expect(screen.getByText("PA")).toBeInTheDocument();
        expect(screen.getByText("AE")).toBeInTheDocument();
        expect(screen.getByText("CO")).toBeInTheDocument();
        expect(screen.getByText("TA")).toBeInTheDocument();
        expect(screen.getByText("DP")).toBeInTheDocument();
    });
});
