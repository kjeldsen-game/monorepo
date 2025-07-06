import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import SignUpView from './SignUpView';
import * as useAuthModule from 'hooks/useAuth';

jest.mock('hooks/useAuth');
const mockedUseAuth = useAuthModule.useAuth as jest.Mock;

jest.mock('../Common/StyledLink', () => (props: any) => <a {...props} />);
jest.mock('../Common/CustomButton', () => (props: any) => <button {...props} />);

describe('SignUpView', () => {
    it('renders all input fields', () => {
        mockedUseAuth.mockReturnValue({ apiSignUp: jest.fn(), loading: false });

        render(<SignUpView />);

        expect(screen.getByLabelText('Email')).toBeInTheDocument();
        expect(screen.getByLabelText('Teamname')).toBeInTheDocument();
        expect(screen.getByLabelText('Password')).toBeInTheDocument();
        expect(screen.getByLabelText('Confirmpassword')).toBeInTheDocument();
    });

    it('renders logo image with correct src and height', () => {
        mockedUseAuth.mockReturnValue({ apiSignUp: jest.fn(), loading: false });

        render(<SignUpView />);
        const img = screen.getByRole('presentation');
        expect(img).toHaveAttribute('src', '/img/loginlogo.png');
        expect(img).toHaveAttribute('height', '70px');
    });

    it('shows loading spinner when loading is true', () => {
        mockedUseAuth.mockReturnValue({ apiSignUp: jest.fn(), loading: true });

        render(<SignUpView />);
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
        expect(screen.queryByRole('button', { name: /sign up/i })).not.toBeInTheDocument();
    });

    it('renders link to sign in', () => {
        mockedUseAuth.mockReturnValue({ apiSignUp: jest.fn(), loading: false });

        render(<SignUpView />);
        const link = screen.getByText('Sign In');
        expect(link).toBeInTheDocument();
        expect(link.closest('a')).toHaveAttribute('href', '/signin');
    });

    it('submits form and calls apiSignUp with correct data', async () => {
        const apiSignUpMock = jest.fn();
        mockedUseAuth.mockReturnValue({ apiSignUp: apiSignUpMock, loading: false });

        render(<SignUpView />);

        await userEvent.type(screen.getByLabelText('Email'), 'user@example.com');
        await userEvent.type(screen.getByLabelText('Teamname'), 'My Team');
        await userEvent.type(screen.getByLabelText('Password'), 'pass123');
        await userEvent.type(screen.getByLabelText('Confirmpassword'), 'pass123');

        fireEvent.click(screen.getByRole('button', { name: /sign up/i }));

        await waitFor(() => {
            expect(apiSignUpMock).toHaveBeenCalledWith('user@example.com', 'pass123', 'pass123', 'My Team');
        });
    });
});
