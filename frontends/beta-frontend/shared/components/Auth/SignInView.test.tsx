import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import SignInView from './SignInView';
import * as useAuthModule from 'hooks/useAuth';

jest.mock('hooks/useAuth');

const mockedUseAuth = useAuthModule.useAuth as jest.Mock;
jest.mock('../Common/StyledLink', () => (props: any) => <a {...props} />);
jest.mock('../Common/CustomButton', () => (props: any) => <button {...props} />);

describe('SignInView', () => {

    beforeEach(() => {
        mockedUseAuth.mockReturnValue({
            apiSignIn: jest.fn(),
            loading: false,
        });
    });

    it('renders logo image with correct src and height', () => {
        render(<SignInView />);
        const logo = screen.getByRole('presentation', { name: '' }); // alt is empty string
        expect(logo).toBeInTheDocument();
        expect(logo).toHaveAttribute('src', '/img/loginlogo.png');
        expect(logo).toHaveAttribute('height', '150px');
    });

    it('renders email and password fields', () => {
        render(<SignInView />);
        expect(screen.getByLabelText('Email')).toBeInTheDocument();
        expect(screen.getByLabelText('Password')).toBeInTheDocument();
    });

    it('renders forgot password link', () => {
        render(<SignInView />);
        expect(screen.getByText('Forgot password?')).toBeInTheDocument();
        expect(screen.getByText('Forgot password?').closest('a')).toHaveAttribute('href', '/');
    });

    it('renders "No account? Sign Up" text with link', () => {
        render(<SignInView />);
        expect(screen.getByText(/No account\?/i)).toBeInTheDocument();

        const signUpLink = screen.getByText('Sign Up');
        expect(signUpLink).toBeInTheDocument();
        expect(signUpLink.closest('a')).toHaveAttribute('href', '/signup');
    });

    it('submits form and calls apiSignIn with correct data', async () => {
        const apiSignInMock = jest.fn();

        mockedUseAuth.mockReturnValue({
            apiSignIn: apiSignInMock,
            loading: false,
        });

        render(<SignInView />);

        const emailInput = screen.getByLabelText('Email');
        const passwordInput = screen.getByLabelText('Password');
        const submitButton = screen.getByRole('button', { name: /sign in/i });

        await userEvent.type(emailInput, 'test@example.com');
        await userEvent.type(passwordInput, 'mypassword');
        fireEvent.click(submitButton);

        await waitFor(() => {
            expect(apiSignInMock).toHaveBeenCalledWith('test@example.com', 'mypassword');
        });
    });

    it('shows loading spinner when loading is true', () => {
        mockedUseAuth.mockReturnValue({
            apiSignIn: jest.fn(),
            loading: true,
        });

        render(<SignInView />);
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
        expect(screen.queryByRole('button', { name: /sign in/i })).not.toBeInTheDocument();
    });

    it('shows submit button when loading is false', () => {
        mockedUseAuth.mockReturnValue({
            apiSignIn: jest.fn(),
            loading: false,
        });

        render(<SignInView />);
        expect(screen.queryByRole('progressbar')).not.toBeInTheDocument();
        expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
    });
});
