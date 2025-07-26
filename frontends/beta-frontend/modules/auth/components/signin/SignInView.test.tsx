import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import SignInView from './SignInView';
import { useAuth } from 'modules/auth/hooks/useAuth';

// Mock the entire module at the top level
jest.mock('modules/auth/hooks/useAuth');

// Cast the mocked hook to jest.Mock for easy mocking
const mockedUseAuth = useAuth as jest.Mock;

describe('SignInView', () => {
    beforeEach(() => {
        // Default mock implementation before each test
        mockedUseAuth.mockReturnValue({
            loading: false,
            handleSignIn: jest.fn(),
        });
    });

    it('renders email and password fields and submit button', () => {
        render(<SignInView />);

        expect(screen.getByRole('textbox', { name: /email/i })).toBeInTheDocument();
        screen.getByLabelText(/^password$/i)
        expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument();
    });

    it('validates email field as required', async () => {
        render(<SignInView />);

        fireEvent.blur(screen.getByRole('textbox', { name: /email/i }));

        await waitFor(() => {
            expect(screen.getByText(/email is required/i)).toBeInTheDocument();
        });
    });

    it('calls handleSignIn with form data on submit', async () => {
        const handleSignInMock = jest.fn();

        mockedUseAuth.mockReturnValue({
            loading: false,
            handleSignIn: handleSignInMock,
        });

        render(<SignInView />);

        fireEvent.change(screen.getByRole('textbox', { name: /email/i }), {
            target: { value: 'test@example.com' },
        });
        fireEvent.change(screen.getByLabelText(/^password$/i), {
            target: { value: 'password123' },
        });

        fireEvent.click(screen.getByRole('button', { name: /sign in/i }));

        await waitFor(() => {
            expect(handleSignInMock).toHaveBeenCalledWith({
                email: 'test@example.com',
                password: 'password123',
            });
        });
    });

    it('shows loading spinner when loading is true', () => {
        mockedUseAuth.mockReturnValue({
            loading: true,
            handleSignIn: jest.fn(),
        });

        render(<SignInView />);

        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });
});
