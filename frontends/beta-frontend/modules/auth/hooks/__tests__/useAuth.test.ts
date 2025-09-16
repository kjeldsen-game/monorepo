import { act, renderHook } from '@testing-library/react';
import { useAuth } from '../../hooks/useAuth';
import * as authApi from '../../services/authApi';
import { useSnackbar } from 'notistack';

jest.mock('../../services/authApi');

jest.mock('next/router', () => ({
  useRouter: () => ({
    push: jest.fn(),
  }),
}));

jest.mock('notistack', () => ({
  useSnackbar: jest.fn(),
}));

describe('useAuth hook', () => {
  const mockUseEnqueueSnackbar = jest.fn();

  beforeEach(() => {
    (useSnackbar as jest.Mock).mockReturnValue({
      enqueueSnackbar: mockUseEnqueueSnackbar,
    });
    jest.clearAllMocks();
  });

  it('calls forgetPassword and sets notification on success', async () => {
    const mockMessage = 'Password reset email sent';
    (authApi.forgetPassword as jest.Mock).mockResolvedValue({
      message: mockMessage,
    });

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.handleForgetPassword({ email: 'test@example.com' });
    });

    expect(authApi.forgetPassword).toHaveBeenCalledWith({
      email: 'test@example.com',
    });
    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(
      mockMessage,
      expect.any(Object),
    );
  });

  it('handles error during forgetPassword', async () => {
    (authApi.forgetPassword as jest.Mock).mockRejectedValue(
      new Error('Request failed'),
    );

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.handleForgetPassword({ email: 'test@example.com' });
    });

    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(
      'Request failed',
      expect.any(Object),
    );
  });

  it('calls resetPassword and sets notification on success', async () => {
    const mockMessage = 'Password reset successful';
    (authApi.resetPassword as jest.Mock).mockResolvedValue({
      message: mockMessage,
    });

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.handleResetPassword({
        token: '123',
        newPassword: 'newpass',
        confirmPassword: 'newpass',
      });
    });

    expect(authApi.resetPassword).toHaveBeenCalledWith({
      token: '123',
      newPassword: 'newpass',
      confirmPassword: 'newpass',
    });
    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith(
      mockMessage,
      expect.any(Object),
    );
  });

  it('handles error during resetPassword', async () => {
    (authApi.resetPassword as jest.Mock).mockRejectedValue(
      new Error('Token invalid'),
    );

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.handleResetPassword({
        token: '123',
        newPassword: 'newpass',
        confirmPassword: 'newpass',
      });
    });

    expect(mockUseEnqueueSnackbar).toHaveBeenCalledWith('Token invalid', {
      variant: 'error',
    });
  });
});
