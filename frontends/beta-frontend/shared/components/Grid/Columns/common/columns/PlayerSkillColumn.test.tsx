import { PlayerSkill } from "@/shared/models/player/PlayerSkill"
import { PlayerSkillColumn } from "./PlayerSkillColumn";
import { render } from "@testing-library/react";

describe("PlayerSkillColumn", () => {
    const mockPlayer = {
        id: 'test-id',
        name: 'name',
        actualSkills: {
            "SCORING": {
                PlayerSkills: {
                    actual: 10,
                    potential: 12,
                    playerSkillRelevance: "CORE"
                }
            },
            "AERIAL": {
                PlayerSkills: {
                    actual: 50,
                    potential: 55,
                    playerSkillRelevance: "CORE"
                }
            },
        }
    }

    const getValue = jest.fn().mockReturnValue(mockPlayer);

    it("Should return column config with width props when isXs is true", () => {
        const col = PlayerSkillColumn(getValue, PlayerSkill.SCORING, PlayerSkill.AERIAL, true);
        expect(col.width).toBe(30);
        expect(col.maxWidth).toBe(30);
        expect(col.minWidth).toBe(30)
    })

    it("Should return column config with width props when isXs is false", () => {
        const col = PlayerSkillColumn(getValue, PlayerSkill.SCORING, PlayerSkill.AERIAL, false);
        expect(col.width).toBeUndefined();
        expect(col.maxWidth).toBeUndefined();
        expect(col.minWidth).toBeUndefined()
    })

    it('returns a column definition with correct field and header', () => {
        const col = PlayerSkillColumn(getValue, PlayerSkill.SCORING, PlayerSkill.AERIAL, false);

        expect(col.field).toBe('SCORING');

        const { getByText } = render(col.renderHeader!({} as any));
        expect(getByText('SC')).toBeInTheDocument();
    });

    it("should render empty cell if both skills not available", () => {
        const col = PlayerSkillColumn(getValue, PlayerSkill.BALL_CONTROL, PlayerSkill.OFFENSIVE_POSITIONING, false);
        const { container } = render(<>{col.renderCell?.({ row: {}, value: undefined } as any)}</>);
        expect(container.textContent?.trim()).toBe('');
    })
})