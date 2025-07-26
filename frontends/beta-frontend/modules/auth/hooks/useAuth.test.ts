import { act, renderHook } from '@testing-library/react';
import { useAuth } from '../hooks/useAuth'; // adjust path as needed
import * as authApi from '../services/authApi';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';

// ✅ Mock dependencies
jest.mock('../services/authApi');
jest.mock('@/shared/contexts/ErrorContext');
jest.mock('@/shared/contexts/NotificationContext');
jest.mock('next/router', () => ({
  useRouter: () => ({
    push: jest.fn(),
  }),
}));

describe('useAuth hook', () => {
  const mockSetError = jest.fn();
  const mockSetNotification = jest.fn();

  beforeEach(() => {
    (useError as jest.Mock).mockReturnValue({ setError: mockSetError });
    (useNotification as jest.Mock).mockReturnValue({
      setNotification: mockSetNotification,
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
    expect(mockSetNotification).toHaveBeenCalledWith(mockMessage);
    expect(mockSetError).toHaveBeenCalledWith(null);
  });

  it('handles error during forgetPassword', async () => {
    (authApi.forgetPassword as jest.Mock).mockRejectedValue(
      new Error('Request failed'),
    );

    const { result } = renderHook(() => useAuth());

    await act(async () => {
      await result.current.handleForgetPassword({ email: 'test@example.com' });
    });

    expect(mockSetError).toHaveBeenCalledWith('Request failed');
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
    expect(mockSetNotification).toHaveBeenCalledWith(mockMessage);
    expect(mockSetError).toHaveBeenCalledWith(null);
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

    expect(mockSetError).toHaveBeenCalledWith('Token invalid');
  });
});
