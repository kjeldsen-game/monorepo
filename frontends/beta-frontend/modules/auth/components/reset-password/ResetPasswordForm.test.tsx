import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ResetPasswordForm from './ResetPasswordForm';
import { useAuth } from 'modules/auth/hooks/useAuth';
import { useSearchParams } from 'next/navigation';

// Mock useAuth hook
jest.mock('modules/auth/hooks/useAuth');
const mockedUseAuth = useAuth as jest.MockedFunction<typeof useAuth>;

// Mock useSearchParams hook
jest.mock('next/navigation', () => ({
    useSearchParams: jest.fn(),
}));
const mockedUseSearchParams = useSearchParams as jest.MockedFunction<typeof useSearchParams>;

describe('ResetPasswordForm', () => {
    const handleResetPasswordMock = jest.fn();

    beforeEach(() => {
        handleResetPasswordMock.mockClear();

        mockedUseAuth.mockReturnValue({
            handleResetPassword: handleResetPasswordMock,
            loading: false,
        } as any);

        mockedUseSearchParams.mockReturnValue({
            get: jest.fn().mockImplementation((key: string) => {
                if (key === 'token') return 'fake-token';
                return null;
            }),
        } as any);
    });

    test('renders password inputs and submit button', () => {
        render(<ResetPasswordForm />);

        expect(screen.getByLabelText(/new password/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/confirm password/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /reset password/i })).toBeInTheDocument();
    });

    test('shows validation errors when submitting empty form', async () => {
        render(<ResetPasswordForm />);

        fireEvent.click(screen.getByRole('button', { name: /reset password/i }));

        await waitFor(() => {
            expect(screen.getByText(/new password is required!/i)).toBeInTheDocument();
            expect(screen.getByText(/confirm password is required!/i)).toBeInTheDocument();
        });
    });

    test('calls handleResetPassword with correct data on submit', async () => {
        render(<ResetPasswordForm />);

        fireEvent.change(screen.getByLabelText(/new password/i), { target: { value: 'NewPass123!' } });
        fireEvent.change(screen.getByLabelText(/confirm password/i), { target: { value: 'NewPass123!' } });

        fireEvent.click(screen.getByRole('button', { name: /reset password/i }));

        await waitFor(() => {
            expect(handleResetPasswordMock).toHaveBeenCalledWith({
                token: 'fake-token',
                newPassword: 'NewPass123!',
                confirmPassword: 'NewPass123!',
            });
        });
    });

    test('displays loading spinner when loading is true', () => {
        mockedUseAuth.mockReturnValue({
            handleResetPassword: handleResetPasswordMock,
            loading: true,
        } as any);

        render(<ResetPasswordForm />);

        expect(screen.queryByRole('button', { name: /reset password/i })).not.toBeInTheDocument();
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });
});
