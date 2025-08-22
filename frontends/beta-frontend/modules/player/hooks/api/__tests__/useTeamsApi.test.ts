import { renderHook } from '@testing-library/react';
import { useTeamsApi } from '../useTeamsApi';
import * as teamApi from '../../../services/teamApi';
import useSWR from 'swr';
import {
  mockUnauthenticatedSession,
  mockAuthenticatedSession,
} from '__mocks__/useSession.mock';

jest.mock('next-auth/react');
jest.mock('../../../services/teamApi');
jest.mock('swr');

describe('useTeamsApi', () => {
  const mockedGetTeams = teamApi.getTeams as jest.Mock;
  const mockedUseSWR = useSWR as jest.Mock;

  beforeEach(() => {
    jest.resetAllMocks();
  });

  it('should not call API if user is unauthenticated', () => {
    mockUnauthenticatedSession();

    mockedUseSWR.mockImplementation((key, fetcher) => ({
      data: null,
      error: null,
      isLoading: false,
    }));
    const { result } = renderHook(() => useTeamsApi());

    expect(mockedUseSWR).toHaveBeenCalledWith(null, expect.any(Function));
  });

  it('should call getTeams if user is authenticated', () => {
    const fakeData = [{ id: 1, name: 'Team A' }];
    mockAuthenticatedSession();

    mockedGetTeams.mockResolvedValue(fakeData);
    mockedUseSWR.mockImplementation((key, fetcher) => ({
      data: fakeData,
      error: null,
      isLoading: false,
    }));

    const { result } = renderHook(() => useTeamsApi());

    expect(mockedUseSWR).toHaveBeenCalledWith(
      expect.any(String), // TEAMS_API
      expect.any(Function),
    );

    expect(result.current.data).toEqual(fakeData);
    expect(result.current.error).toBeNull();
    expect(result.current.isLoading).toBe(false);
  });

  it('should return error if SWR returns error', () => {
    const fakeError = new Error('Failed');
    mockAuthenticatedSession();

    mockedUseSWR.mockImplementation(() => ({
      data: null,
      error: fakeError,
      isLoading: false,
    }));

    const { result } = renderHook(() => useTeamsApi());

    expect(result.current.error).toEqual(fakeError);
  });
});
