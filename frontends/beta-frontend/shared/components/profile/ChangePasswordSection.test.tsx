import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import { useProfileRepository } from '@/pages/api/profile/useProfileRepository';
import { useSession } from 'next-auth/react';
import ChangePasswordSection from './ChangePasswordSection';

jest.mock('next-auth/react');
jest.mock('@/pages/api/profile/useProfileRepository');

describe('ChangePasswordSection', () => {
    const mockChangePassword = jest.fn();

    beforeEach(() => {
        (useSession as jest.Mock).mockReturnValue({
            data: { accessToken: 'mock-token' },
        });

        (useProfileRepository as jest.Mock).mockReturnValue({
            changePassword: mockChangePassword,
        });
    });

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('renders password fields and button', () => {
        render(<ChangePasswordSection />);

        expect(screen.getByLabelText(/current password/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/new password/i)).toBeInTheDocument();
        expect(screen.getByLabelText(/confirm password/i)).toBeInTheDocument();
        expect(screen.getByRole('button', { name: /change password/i })).toBeInTheDocument();
    });

    it('calls changePassword with input values on button click', () => {
        render(<ChangePasswordSection />);

        const current = screen.getByLabelText(/current password/i);
        const newPass = screen.getByLabelText(/new password/i);
        const confirm = screen.getByLabelText(/confirm password/i);
        const button = screen.getByRole('button', { name: /change password/i });

        fireEvent.change(current, { target: { value: 'old123' } });
        fireEvent.change(newPass, { target: { value: 'new123' } });
        fireEvent.change(confirm, { target: { value: 'new123' } });
        fireEvent.click(button);

        expect(mockChangePassword).toHaveBeenCalledWith({
            oldPassword: 'old123',
            newPassword: 'new123',
            confirmPassword: 'new123',
        });
    });
});
