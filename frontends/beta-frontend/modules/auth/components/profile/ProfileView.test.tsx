import { render, screen } from '@testing-library/react';
import ProfileView from './ProfileView';
import { useProfile } from 'modules/auth/hooks/useProfile';

jest.mock('modules/auth/hooks/useProfile');

describe('ProfileView', () => {
    beforeEach(() => {

        (useProfile as jest.Mock).mockReturnValue({
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
        expect(screen.getByRole('img')).toBeInTheDocument();
    });
});
