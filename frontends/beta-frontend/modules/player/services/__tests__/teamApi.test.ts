import { getTeams, getTeam, getLineupValidation, TEAMS_API } from '../teamApi';

import { connectorAPI } from '@/libs/fetcher';

jest.mock('@/libs/fetcher', () => ({
  connectorAPI: jest.fn(),
}));

export const mockedConnectorAPI = connectorAPI as jest.Mock;

describe('teamApi', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('calls connectorAPI correctly in getTeams', async () => {
    mockedConnectorAPI.mockResolvedValueOnce([{ id: 't1' }]);

    const res = await getTeams('token123');

    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      `${TEAMS_API}?page=0&size=99`,
      'GET',
      undefined,
      undefined,
      'token123',
    );
    expect(res).toEqual([{ id: 't1' }]);
  });

  it('returns undefined if teamId or token is null in getTeam', () => {
    expect(getTeam(null, 'token')).toBeUndefined();
    expect(getTeam('123', null)).toBeUndefined();
  });

  it('calls connectorAPI correctly in getTeam', () => {
    getTeam('123', 'token123');
    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      `${TEAMS_API}/123`,
      'GET',
      undefined,
      undefined,
      'token123',
    );
  });

  it('returns undefined if teamId or token is null in getLineupValidation', () => {
    expect(getLineupValidation(null, 'token')).toBeUndefined();
    expect(getLineupValidation('123', null)).toBeUndefined();
  });

  it('calls connectorAPI correctly in getLineupValidation', () => {
    getLineupValidation('123', 'token123');
    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      `${TEAMS_API}/123/validate`,
      'GET',
      undefined,
      undefined,
      'token123',
    );
  });
});
