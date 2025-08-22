import teamMockData from '../../../__fixtures__/team.fixture.json';
import { renderHook } from '@testing-library/react';
import { useLineupBuilder } from '../useLineupBuilder';
import { Player } from '@/shared/models/player/Player';

const mockPlayers = teamMockData.players;

describe('useLineupBuilder', () => {
  it('should build lineup and bench correctly', () => {
    const { result } = renderHook(() =>
      useLineupBuilder(mockPlayers as Player[]),
    );

    const { lineup, bench } = result.current;

    expect(lineup).toBeDefined();
    expect(lineup.CENTRE_BACK.length).toBe(3);
    expect(lineup.DEFENSIVE_MIDFIELDER.length).toBe(2);
    expect(lineup.GOALKEEPER).toBeDefined();

    expect(bench.length).toBe(7);
  });

  it('should handle empty players array', () => {
    const { result } = renderHook(() => useLineupBuilder([]));
    const { lineup, bench } = result.current;

    expect(lineup).toEqual({});
    expect(bench).toBeUndefined();
  });
});
