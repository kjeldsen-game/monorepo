import { renderHook } from '@testing-library/react';
import { useEventBuilder } from '../useEventBuilder';
import type { Play } from 'modules/match/types/MatchResponses';

describe('useEventBuilder', () => {
  const mockPlays: Play[] = [
    {
      action: 'PASS',
      duel: { initiator: { id: 'player1' } },
    } as any,
    {
      action: 'PASS',
      duel: { initiator: { id: 'player1' } },
    } as any,
    {
      action: 'SHOOT',
      duel: { initiator: { id: 'player2' } },
    } as any,
    {
      action: 'PASS',
      duel: { initiator: { id: 'player3' } },
    } as any,
  ];

  it('groups plays into possessions by initiator id', () => {
    const { result } = renderHook(() => useEventBuilder(mockPlays));

    expect(result.current.loading).toBe(false);
    expect(result.current.possesions).toHaveLength(3);

    expect(result.current.possesions[0]).toHaveLength(2);
    expect(result.current.possesions[0][0].action).toBe('PASS');
    expect(result.current.possesions[0][1].action).toBe('PASS');

    expect(result.current.possesions[1][0].action).toBe('SHOOT');

    expect(result.current.possesions[2][0].action).toBe('PASS');
  });

  it('extracts highlights from plays with action SHOOT', () => {
    const { result } = renderHook(() => useEventBuilder(mockPlays));

    expect(result.current.highlights).toHaveLength(1);
    expect(result.current.highlights[0][0].action).toBe('SHOOT');
  });
});
