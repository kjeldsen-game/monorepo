import React from 'react';
import { render, screen } from '@testing-library/react';
import ChangeProfileInformationSection from './ChangeProfileInformationSection';

describe('ChangeProfileInformationSection', () => {

    it('renders team name and email fields with provided values', () => {
        render(<ChangeProfileInformationSection teamName="Team Rocket" email="rocket@team.com" />);

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
        render(<ChangeProfileInformationSection />);

        expect((screen.getByLabelText('Team Name') as HTMLInputElement).value).toBe('');
        expect((screen.getByLabelText('Email Address') as HTMLInputElement).value).toBe('');
    });
});
