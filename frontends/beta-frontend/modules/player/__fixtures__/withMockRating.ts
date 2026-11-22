import { Player } from '@/shared/models/player/Player';

// team.fixture.json predates the `rating` field on Player; synthesize one from skill averages
// so screens that read player.rating (e.g. RatingColumn) don't crash in this local mock.
export function withMockRating(players: any[]): Player[] {
  return players.map((player) => {
    const skills = Object.values(player.actualSkills ?? {}) as { PlayerSkills: { actual: number; potential: number } }[];
    const total = skills.reduce(
      (acc, skill) => ({
        actual: acc.actual + skill.PlayerSkills.actual,
        potential: acc.potential + skill.PlayerSkills.potential,
      }),
      { actual: 0, potential: 0 },
    );
    const count = skills.length || 1;
    return {
      ...player,
      rating: {
        actual: Math.round(total.actual / count),
        potential: Math.round(total.potential / count),
      },
    };
  });
}
