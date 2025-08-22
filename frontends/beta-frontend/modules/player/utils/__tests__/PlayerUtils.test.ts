import { mockPlayers } from 'modules/player/__fixtures__/player.fixture';
import { PlayerLineupStatus } from '@/shared/models/player/PlayerLineupStatus';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { filterPlayersByField } from '../PlayerUtils';

describe('filterPlayersByField', () => {
  it('filters players by status', () => {
    const activePlayers = filterPlayersByField(
      mockPlayers,
      'status',
      PlayerLineupStatus.ACTIVE,
    );
    expect(activePlayers).toHaveLength(1);
    expect(activePlayers[0].name).toBe('Willy Treutel');
  });

  it('filters players by preferredPosition', () => {
    const forwards = filterPlayersByField(
      mockPlayers,
      'preferredPosition',
      PlayerPosition.FORWARD,
    );
    expect(forwards).toHaveLength(2);
    expect(forwards.map((p) => p.name)).toEqual(
      expect.arrayContaining(['Willy Treutel', 'Willy Two']),
    );
  });

  it('filters players by actual position', () => {
    const centreBacks = filterPlayersByField(
      mockPlayers,
      'position',
      PlayerPosition.CENTRE_BACK,
    );
    expect(centreBacks).toHaveLength(1);
    expect(centreBacks[0].name).toBe('Willy Two');
  });
});
