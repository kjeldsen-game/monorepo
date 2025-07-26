import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import SignUpView from './SignUpView';
import { useAuth } from 'modules/auth/hooks/useAuth';

// Mock MUI components that depend on refs or styles
jest.mock('@mui/material', () => {
    const original = jest.requireActual('@mui/material');
    return {
        ...original,
        CircularProgress: () => <div data-testid="loading-spinner" />,
    };
});

// Mock child components
jest.mock('../common/PasswordInput', () => (props: any) => (
    <input
        type="password"
        name={props.name}
        placeholder={props.label}
        onChange={(e) => props.control.setValue(props.name, e.target.value)}
    />
));

jest.mock('../common/TextInput', () => (props: any) => (
    <input
        name={props.name}
        placeholder={props.label}
        onChange={(e) => props.control.setValue(props.name, e.target.value)}
    />
));

// Mock useAuth hook
const mockHandleSignUp = jest.fn();
jest.mock('modules/auth/hooks/useAuth', () => ({
    useAuth: () => ({
        handleSignUp: mockHandleSignUp,
        loading: false,
    }),
}));

describe('SignUpView', () => {
    beforeEach(() => {
        mockHandleSignUp.mockClear();
    });

    it('renders sign up form', () => {
        render(<SignUpView />);
        expect(screen.getByText(/Sign Up/i)).toBeInTheDocument();
        expect(screen.getByPlaceholderText(/Email/i)).toBeInTheDocument();
        expect(screen.getByPlaceholderText(/Team Name/i)).toBeInTheDocument();
        expect(screen.getByPlaceholderText(/^Password/i)).toBeInTheDocument();
        expect(screen.getByPlaceholderText(/Confirm Password/i)).toBeInTheDocument();
    });
});
