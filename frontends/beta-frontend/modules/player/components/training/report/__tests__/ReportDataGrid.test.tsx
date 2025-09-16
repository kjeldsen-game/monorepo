import { render, screen } from '@testing-library/react';
import ReportDataGrid from '../ReportDataGrid';
import { TrainingType } from "modules/player/types/TrainingResponses";

const mockTrainings: any[] = [
    {
        player: { name: "Player A" },
        skill: "PASSING",
        pointsAfterTraining: 11,
        trainingType: TrainingType.PLAYER_TRAINING,
    },
    {
        player: { name: "Player B" },
        skill: "SHOOTING",
        pointsAfterTraining: 15,
        trainingType: TrainingType.PLAYER_TRAINING,
    },
    {
        player: { name: "Player C" },
        skill: "DEFENSE",
        pointsAfterTraining: 5,
        trainingType: TrainingType.DECLINE_TRAINING,
    },
];

describe("ReportDataGrid", () => {
    it("renders the grid with trainings", () => {
        render(<ReportDataGrid trainings={mockTrainings} />);

        const grid = screen.getByRole("grid");
        expect(grid).toBeInTheDocument();

        expect(screen.getByText("Player A")).toBeInTheDocument();
        expect(screen.getByText("Player B")).toBeInTheDocument();
        expect(screen.getByText("Player C")).toBeInTheDocument();
    });

    it("applies correct row class for last rows of each type", () => {
        render(<ReportDataGrid trainings={mockTrainings} />);

        const playerBRow = screen.getByText("Player B").closest(".MuiDataGrid-row");
        expect(playerBRow).toHaveClass("playerTraining-last");

        const playerCRow = screen.getByText("Player C").closest(".MuiDataGrid-row");
        expect(playerCRow).toHaveClass("declineTraining-last");

        const playerARow = screen.getByText("Player A").closest(".MuiDataGrid-row");
        expect(playerARow).not.toHaveClass("playerTraining-last");
    });
});
