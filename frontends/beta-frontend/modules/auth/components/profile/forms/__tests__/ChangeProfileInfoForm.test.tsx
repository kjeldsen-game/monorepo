import { render, screen } from '@testing-library/react';
import ChangeProfileInfoForm from '../ChangeProfileInfoForm';

describe('ChangeProfileInformationSection', () => {

    it('renders team name and email fields with provided values', () => {
        render(<ChangeProfileInfoForm teamName="Team Rocket" email="rocket@team.com" />);

        const teamNameInput = screen.getByLabelText('Team Name') as HTMLInputElement;
        expect(teamNameInput).toBeInTheDocument();
        expect(teamNameInput.value).toBe('Team Rocket');
        expect(teamNameInput).toBeDisabled();

        const emailInput = screen.getByLabelText('Email Address') as HTMLInputElement;
        expect(emailInput).toBeInTheDocument();
        expect(emailInput.value).toBe('rocket@team.com');
        expect(emailInput).toBeDisabled();

        expect(screen.getByRole('button', { name: /save changes/i })).toBeDisabled();
    });

    it('renders empty fields when no props are passed', () => {
        render(<ChangeProfileInfoForm />);

        expect((screen.getByLabelText('Team Name') as HTMLInputElement).value).toBe('');
        expect((screen.getByLabelText('Email Address') as HTMLInputElement).value).toBe('');
    });
});
