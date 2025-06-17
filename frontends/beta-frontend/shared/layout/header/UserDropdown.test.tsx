import { render, screen, fireEvent } from '@testing-library/react';
import UserDropdown from './UserDropdown';
import { signOut } from 'next-auth/react';

jest.mock('next-auth/react', () => ({
    signOut: jest.fn(),
}));

describe('UserDropdown', () => {
    const user = { id: '123', email: 'test@example.com' };

    it('shows dropdown menu when button is clicked', () => {
        render(<UserDropdown user={user} />);

        const userButton = screen.getByRole('button', { name: user.email });
        expect(userButton).toBeInTheDocument();

        fireEvent.click(userButton);

        // Buttons in dropdown menu are visible
        expect(screen.getByText(/profile/i)).toBeVisible();
        expect(screen.getByText(/sign out/i)).toBeVisible();
    });

    it('calls signOut when "Sign Out" is clicked', () => {
        render(<UserDropdown user={user} />);

        const userButton = screen.getByRole('button', { name: user.email });
        fireEvent.click(userButton);

        const signOutButton = screen.getByText(/sign out/i);
        fireEvent.click(signOutButton);

        expect(signOut).toHaveBeenCalled();
    });
});
