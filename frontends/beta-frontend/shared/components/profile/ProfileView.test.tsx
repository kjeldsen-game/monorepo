import { render, screen } from '@testing-library/react';
import { useSession } from 'next-auth/react';
import { useProfileRepository } from '@/pages/api/profile/useProfileRepository';
import ProfileView from './ProfileView';

jest.mock('next-auth/react');
jest.mock('@/pages/api/profile/useProfileRepository');

describe('ProfileView', () => {
    beforeEach(() => {
        (useSession as jest.Mock).mockReturnValue({
            data: { accessToken: 'fake-token' },
            status: 'authenticated',
        });

        (useProfileRepository as jest.Mock).mockReturnValue({
            data: {
                avatar: 'https://example.com/avatar.png',
                teamName: 'Team Rocket',
                email: 'team@rocket.com',
            },
        });
    });

    it('renders profile info and sections correctly', () => {
        render(<ProfileView />);

        expect(screen.getByText('Profile Information')).toBeInTheDocument();

        expect(screen.getByLabelText('Team Name')).toHaveValue('Team Rocket');
        expect(screen.getByLabelText('Email Address')).toHaveValue('team@rocket.com');

        expect(
            screen.getByText(/the app is not continuously deployed/i)
        ).toBeInTheDocument();

        expect(screen.getByRole('img')).toBeInTheDocument();
    });
});
