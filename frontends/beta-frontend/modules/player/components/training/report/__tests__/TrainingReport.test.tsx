import { render, screen } from "@testing-library/react";
import TrainingReport from "../TrainingReport";
import { TrainingEventResponse } from "modules/player/types/TrainingResponses";

jest.mock("../../columns/TrainingColumns", () => ({
    TrainingColumns: jest.fn(() => [{ field: "skill", headerName: "Skill" }]),
}));

jest.mock("@/shared/components/Grid/Grid", () => (props: any) => (
    <div data-testid="training-report-grid" {...props} />
));

describe("TrainingReport", () => {
    const mockTrainings: TrainingEventResponse[] = [
        {
            player: { name: "John Doe" },
            skill: "strength",
        } as any,
        {
            player: { name: "Jane Smith" },
            skill: "speed",
        } as any,
    ];

    it("renders the Accordion with the correct date in the title", () => {
        render(<TrainingReport date="2025-08-29" trainings={mockTrainings} />);

        expect(
            screen.getByText(/Training Report from 2025-08-29/i)
        ).toBeInTheDocument();
    });

    it("renders the Grid with rows", () => {
        render(<TrainingReport date="2025-08-29" trainings={mockTrainings} />);

        const grid = screen.getByTestId("training-report-grid");
        expect(grid).toBeInTheDocument();
    });
});
