// UserMenu.test.tsx
import { render, screen, fireEvent } from '@testing-library/react';
import UserMenu from '../UserMenu';
import { signOut } from 'next-auth/react';

// Mock NextAuth signOut function
jest.mock('next-auth/react', () => ({
    signOut: jest.fn(),
}));

describe('UserMenu', () => {
    const mockUser = { email: 'test@example.com' };

    it('renders the user button with email', () => {
        render(<UserMenu user={mockUser} />);
        expect(screen.getByText('Account')).toBeInTheDocument();
        expect(screen.getByText(mockUser.email)).toBeInTheDocument();
    });

    it('opens menu when button is clicked', () => {
        render(<UserMenu user={mockUser} />);
        const button = screen.getByText('Account');
        fireEvent.click(button);

        expect(screen.getByText('Profile')).toBeInTheDocument();
        expect(screen.getByText('Sign Out')).toBeInTheDocument();
    });

    it('calls signOut when Sign Out is clicked', () => {
        render(<UserMenu user={mockUser} />);
        const button = screen.getByText('Account');
        fireEvent.click(button);

        const signOutItem = screen.getByText('Sign Out');
        fireEvent.click(signOutItem);

        expect(signOut).toHaveBeenCalled();
    });

    it('does not crash when user is undefined', () => {
        render(<UserMenu />);
        expect(screen.getByText('Account')).toBeInTheDocument();
    });
});
