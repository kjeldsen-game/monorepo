import { render, fireEvent } from "@testing-library/react";
import { TrainingSkillColumn } from "../TrainingSkillColumn";
import { PlayerSkill, PlayerSkillToShortcut } from "@/shared/models/player/PlayerSkill";

jest.mock(
    "@/shared/components/Grid/Columns/common/components/PlayerSkillText",
    () => ({ skills, ...props }: any) => (
        <div
            data-testid="player-skill-text" {...props}>
            {skills.primarySkill}
        </div>
    )
);

describe("TrainingSkillColumn", () => {
    const mockHandleCellClick = jest.fn();
    const getValue = (row: any) => row.player;

    const primarySkill = PlayerSkill.AERIAL;
    const secondarySkill = PlayerSkill.BALL_CONTROL;

    const columnDef = TrainingSkillColumn(
        getValue,
        primarySkill,
        secondarySkill,
        mockHandleCellClick
    );

    const mockRow = {
        player: {
            id: "player-1",
            actualSkills: {
                [primarySkill]: { primarySkill, value: 10 },
            },
        },
        skills: [primarySkill],
    };

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("renderHeader returns JSX containing the skill shortcut", () => {
        const { container } = render(<>{columnDef.renderHeader!({ row: mockRow } as any)}</>);
        expect(container.textContent).toContain(PlayerSkillToShortcut[primarySkill]);
        expect(container.textContent).toContain(PlayerSkillToShortcut[secondarySkill]);
    });

    it("renderCell renders PlayerSkillText", () => {
        const { getByTestId } = render(<>{columnDef.renderCell!({ row: mockRow } as any)}</>);
        expect(getByTestId("player-skill-text")).toBeInTheDocument();
    });

    it("renderCell calls handleCellClick on click when skill exists", () => {
        const { getByTestId } = render(<>{columnDef.renderCell!({ row: mockRow } as any)}</>);
        const skillDiv = getByTestId("player-skill-text").parentElement!;
        fireEvent.click(skillDiv);

        expect(mockHandleCellClick).toHaveBeenCalledWith(
            primarySkill,
            undefined,
            "player-1"
        );
    });
});
