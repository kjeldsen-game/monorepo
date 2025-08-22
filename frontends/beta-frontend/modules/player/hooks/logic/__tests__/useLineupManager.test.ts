import { useLineupManager } from '../useLineupManager';
import { Player } from '@/shared/models/player/Player';
import { TeamModifiers, Tactic } from '@/shared/models/player/TeamModifiers';
import teamMockData from '../../../__fixtures__/team.fixture.json';
import { act, renderHook } from '@testing-library/react';

jest.mock('../../api/useTeamApi', () => ({
  useTeamApi: () => ({
    handleLineupUpdateRequest: jest.fn(),
  }),
}));

describe('useLineupManager', () => {
  const team = teamMockData;

  const mockUpdateFn = jest.fn();

  it('should initialize state from props', () => {
    const { result } = renderHook(() =>
      useLineupManager(
        team?.players as Player[],
        team?.teamModifiers as TeamModifiers,
        mockUpdateFn,
      ),
    );

    expect(result.current.players).toEqual(team?.players);
    expect(result.current.teamModifiers).toEqual(team?.teamModifiers);
  });

  it('should call updateFn when handleTeamModifierChange is called', () => {
    const { result } = renderHook(() =>
      useLineupManager(
        team?.players as Player[],
        team?.teamModifiers as TeamModifiers,
        mockUpdateFn,
      ),
    );

    act(() => {
      result.current.handleTeamModifierChange(Tactic.CATENACCIO, 'tactic');
    });

    expect(result.current.teamModifiers.tactic).toBe(Tactic.CATENACCIO);
    expect(mockUpdateFn).toHaveBeenCalledWith({
      players: team?.players,
      teamModifiers: expect.objectContaining({ tactic: Tactic.CATENACCIO }),
    });
  });

  it('should update player status to BENCH', () => {
    const { result } = renderHook(() =>
      useLineupManager(
        team?.players as Player[],
        team?.teamModifiers as TeamModifiers,
        mockUpdateFn,
      ),
    );

    const newPlayer = { ...team?.players[0] };

    act(() => {
      result.current.handleEdit(
        newPlayer as Player,
        undefined,
        undefined,
        false,
      );
    });

    expect(result.current.players[0].status).toBe('BENCH');
    expect(mockUpdateFn).toHaveBeenCalled();
  });

  it('should swap players if oldPlayer exists', () => {
    const { result } = renderHook(() =>
      useLineupManager(
        team?.players as Player[],
        team?.teamModifiers as TeamModifiers,
        mockUpdateFn,
      ),
    );

    const newPlayer = { ...team?.players[0], name: 'Alice Updated' };
    const oldPlayer = { ...team?.players[0] };

    act(() => {
      result.current.handleEdit(newPlayer as Player, oldPlayer as Player);
    });

    expect(result.current.players[0].name).toBe('Alice Updated');
    expect(mockUpdateFn).toHaveBeenCalled();
  });
});
