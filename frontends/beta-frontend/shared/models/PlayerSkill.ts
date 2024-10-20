export enum PlayerSkill {
    SCORING = "SCORING",
    OFFENSIVE_POSITIONING = "OFFENSIVE_POSITIONING",
    BALL_CONTROL = "BALL_CONTROL",
    PASSING = "PASSING",
    AERIAL = "AERIAL",
    CONSTITUTION = "CONSTITUTION",
    TACKLING = "TACKLING",
    DEFENSIVE_POSITIONING = "DEFENSIVE_POSITIONING",
    REFLEXES = "REFLEXES",
    GOALKEEPER_POSITIONING = "GOALKEEPER_POSITIONING",
    INTERCEPTIONS = "INTERCEPTIONS",
    CONTROL = "CONTROL",
    ORGANIZATION = "ORGANIZATION",
    ONE_ON_ONE = "ONE_ON_ONE",
    INTERCEPTING = "INTERCEPTING" // Currently for engine use only, ignore
}

interface PlayerSkillShortcutsMap {
    [key: string]: PlayerSkill;
}

export const PlayerSkillShortcuts: PlayerSkillShortcutsMap = {
    SC: PlayerSkill.SCORING,
    OP: PlayerSkill.OFFENSIVE_POSITIONING,
    BC: PlayerSkill.BALL_CONTROL,
    PA: PlayerSkill.PASSING,
    AE: PlayerSkill.AERIAL,
    CO: PlayerSkill.CONSTITUTION,
    TA: PlayerSkill.TACKLING,
    DP: PlayerSkill.DEFENSIVE_POSITIONING,
    RE: PlayerSkill.REFLEXES,
    GP: PlayerSkill.GOALKEEPER_POSITIONING,
    IN: PlayerSkill.INTERCEPTIONS,
    CN: PlayerSkill.CONTROL,
    OR: PlayerSkill.ORGANIZATION,
    OO: PlayerSkill.ONE_ON_ONE,
    INT: PlayerSkill.INTERCEPTING, // Currently for engine use only, ignore
};

export const PlayerSkillToShortcut: Record<PlayerSkill, string> = 
    Object.entries(PlayerSkillShortcuts).reduce((acc, [shortcut, skill]) => {
        acc[skill as PlayerSkill] = shortcut;
        return acc;
    }, {} as Record<PlayerSkill, string>);
