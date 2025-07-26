import { useProfile } from './useProfile';
import * as profileApi from '../services/profileApi';
import { useSession } from 'next-auth/react';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import useSWR from 'swr';
import { act, renderHook } from '@testing-library/react';

jest.mock('../services/profileApi');
jest.mock('next-auth/react');
jest.mock('@/shared/contexts/ErrorContext');
jest.mock('@/shared/contexts/NotificationContext');
jest.mock('swr');

describe('useProfile', () => {
  const mockSetError = jest.fn();
  const mockSetNotification = jest.fn();
  const mockMutate = jest.fn();

  const mockAccessToken = 'mocked-token';

  beforeEach(() => {
    (useSession as jest.Mock).mockReturnValue({
      data: { accessToken: mockAccessToken },
    });

    (useError as jest.Mock).mockReturnValue({ setError: mockSetError });
    (useNotification as jest.Mock).mockReturnValue({
      setNotification: mockSetNotification,
    });

    (useSWR as jest.Mock).mockReturnValue({
      data: { name: 'John Doe', email: 'john@example.com' },
      error: null,
      isLoading: false,
      mutate: mockMutate,
    });

    jest.clearAllMocks();
  });

  it('returns profile data correctly', () => {
    const { result } = renderHook(() => useProfile());

    expect(result.current.data).toEqual({
      name: 'John Doe',
      email: 'john@example.com',
    });
    expect(result.current.error).toBeNull();
    expect(result.current.isLoading).toBe(false);
  });

  it('calls changePassword and shows notification', async () => {
    const mockResponse = { message: 'Password changed successfully' };
    (profileApi.changePassword as jest.Mock).mockResolvedValue(mockResponse);

    const { result } = renderHook(() => useProfile());

    await act(async () => {
      await result.current.handleChangePassword({
        oldPassword: 'oldpass',
        newPassword: 'newpass',
        confirmPassword: 'newpass',
      });
    });

    expect(profileApi.changePassword).toHaveBeenCalledWith(
      {
        oldPassword: 'oldpass',
        newPassword: 'newpass',
        confirmPassword: 'newpass',
      },
      mockAccessToken,
    );
    expect(mockSetNotification).toHaveBeenCalledWith(mockResponse.message);
    expect(mockSetError).not.toHaveBeenCalled();
  });

  it('handles error in changePassword', async () => {
    (profileApi.changePassword as jest.Mock).mockRejectedValue(
      new Error('Change password failed'),
    );

    const { result } = renderHook(() => useProfile());

    await act(async () => {
      await result.current.handleChangePassword({
        oldPassword: 'old',
        newPassword: 'new',
        confirmPassword: 'new',
      });
    });

    expect(mockSetError).toHaveBeenCalledWith('Change password failed');
  });

  it('calls changeAvatar and refreshes profile on success', async () => {
    const mockResponse = { message: 'Avatar updated successfully' };
    (profileApi.changeAvatar as jest.Mock).mockResolvedValue(mockResponse);

    const { result } = renderHook(() => useProfile());

    const formData = new FormData();
    formData.append('avatar', new File([''], 'avatar.png'));

    await act(async () => {
      await result.current.handleChangeAvatar(formData);
    });

    expect(profileApi.changeAvatar).toHaveBeenCalledWith(
      formData,
      mockAccessToken,
    );
    expect(mockSetNotification).toHaveBeenCalledWith(mockResponse.message);
    expect(mockMutate).toHaveBeenCalled();
  });

  it('handles error in changeAvatar', async () => {
    (profileApi.changeAvatar as jest.Mock).mockRejectedValue(
      new Error('Avatar update failed'),
    );

    const { result } = renderHook(() => useProfile());

    const formData = new FormData();
    formData.append('avatar', new File([''], 'avatar.png'));

    await act(async () => {
      await result.current.handleChangeAvatar(formData);
    });

    expect(mockSetError).toHaveBeenCalledWith('Avatar update failed');
  });
});
