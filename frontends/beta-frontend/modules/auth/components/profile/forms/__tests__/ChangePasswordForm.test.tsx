import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import ChangePasswordForm from '../ChangePasswordForm';

// Mock the whole module
jest.mock('modules/auth/hooks/useProfile');

import { useProfile } from 'modules/auth/hooks/useProfile';

describe('ChangePasswordForm', () => {
    const handleChangePasswordMock = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
        // Cast useProfile as a jest mock function to mock its implementation
        (useProfile as jest.Mock).mockReturnValue({
            handleChangePassword: handleChangePasswordMock,
        });
    });

    test('renders all password inputs and submit button', () => {
        render(<ChangePasswordForm />);

        expect(screen.getByLabelText(/current password/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/new password/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/confirm password/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /change password/i })).toBeInTheDocument();
    });

    test('shows validation errors when submitted empty', async () => {
        render(<ChangePasswordForm />);

        fireEvent.click(screen.getByRole('button', { name: /change password/i }));

        await waitFor(() => {
            expect(screen.getByText(/current password is required!/i)).toBeInTheDocument();
            expect(screen.getByText(/new password is required!/i)).toBeInTheDocument();
            expect(screen.getByText(/confirm password is required!/i)).toBeInTheDocument();
        });
    });

    test('submits the form with correct data', async () => {
        render(<ChangePasswordForm />);

        fireEvent.change(screen.getByLabelText(/current password/i), { target: { value: 'oldPass123' } });
        fireEvent.change(screen.getByLabelText(/new password/i), { target: { value: 'newPass123' } });
        fireEvent.change(screen.getByLabelText(/confirm password/i), { target: { value: 'newPass123' } });

        fireEvent.click(screen.getByRole('button', { name: /change password/i }));

        await waitFor(() => {
            expect(handleChangePasswordMock).toHaveBeenCalledTimes(1);
            expect(handleChangePasswordMock).toHaveBeenCalledWith({
                oldPassword: 'oldPass123',
                newPassword: 'newPass123',
                confirmPassword: 'newPass123',
            });
        });
    });
});
