import { useProfile } from '../useProfile';
import * as profileApi from '../../services/profileApi';
import { useSession } from 'next-auth/react';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import useSWR from 'swr';
import { act, renderHook } from '@testing-library/react';
import { useSnackbar } from 'notistack';

jest.mock('../../services/profileApi');
jest.mock('next-auth/react');
jest.mock('@/shared/contexts/ErrorContext');
jest.mock('@/shared/contexts/NotificationContext');
jest.mock('swr');

jest.mock('notistack', () => ({
  useSnackbar: jest.fn(),
}));

describe('useProfile', () => {
  const mockUseEnqueueSnackbar = jest.fn();
  const mockMutate = jest.fn();

  const mockAccessToken = 'mocked-token';

  beforeEach(() => {
    (useSnackbar as jest.Mock).mockReturnValue({
      enqueueSnackbar: mockUseEnqueueSnackbar,
    });
    (useSession as jest.Mock).mockReturnValue({
      data: { accessToken: mockAccessToken },
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
    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(mockResponse.message, {
      variant: 'success',
    });
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

    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(
      'Change password failed',
      {
        variant: 'error',
      },
    );
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
    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(mockResponse.message, {
      variant: 'success',
    });
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

    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(
      'Avatar update failed',
      {
        variant: 'error',
      },
    );
  });
});
