import { render, screen, fireEvent } from "@testing-library/react";
import TrainingDialog from "../TrainingDialog";
import { PlayerSkill } from "@/shared/models/player/PlayerSkill";

jest.mock("@/shared/utils/StringUtils", () => ({
    convertSnakeCaseToTitleCase: (val: string) => val,
}));

const mockHandleScheduleTrainingRequest = jest.fn();
jest.mock("modules/player/hooks/api/useTrainingApi", () => ({
    useTrainingApi: () => ({
        handleScheduleTrainingRequest: mockHandleScheduleTrainingRequest,
    }),
}));

describe("TrainingDialog", () => {
    const defaultProps = {
        open: true,
        handleClose: jest.fn(),
        skillToTrain: PlayerSkill.AERIAL,
        playerId: "player-1",
    };

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("renders correct text when no skill is under training", () => {
        render(<TrainingDialog {...defaultProps} skillUnderTraining={undefined} />);

        expect(
            screen.getByText(/Are you sure that you want to schedule training for AERIAL/i)
        ).toBeInTheDocument();
    });

    it("renders correct text when skill is under training", () => {
        render(<TrainingDialog {...defaultProps} skillUnderTraining={PlayerSkill.BALL_CONTROL} />);

        expect(
            screen.getByText(
                /Are you sure that you want to override your current older scheduled training of BALL_CONTROL for AERIAL/i
            )
        ).toBeInTheDocument();
    });

    it("calls training request and closes modal on button click", () => {
        render(<TrainingDialog {...defaultProps} skillUnderTraining={undefined} />);

        const button = screen.getByRole("button", { name: /Schedule Traininig/i });
        fireEvent.click(button);

        expect(mockHandleScheduleTrainingRequest).toHaveBeenCalledWith(
            { skill: PlayerSkill.AERIAL },
            "player-1"
        );
        expect(defaultProps.handleClose).toHaveBeenCalled();
    });
});
