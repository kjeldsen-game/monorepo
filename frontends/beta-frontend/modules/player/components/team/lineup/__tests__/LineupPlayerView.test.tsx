import React from 'react';
import { render, screen } from '@testing-library/react';
import LineupPlayerView from '../LineupPlayerView';
import { Lineup } from 'modules/player/types/Lineup';
import { LineupProvider } from 'modules/player/contexts/LineupContext';

const mockLineup = {
    GOALKEEPER: { name: 'GK Player' },
    LEFT_BACK: { name: 'LB Player' },
    CENTRE_BACK: [{ name: 'CB1' }, { name: 'CB2' }],
    RIGHT_BACK: { name: 'RB Player' },
    LEFT_WINGBACK: { name: 'LWB Player' },
    DEFENSIVE_MIDFIELDER: [{ name: 'DM1' }, { name: 'DM2' }],
    RIGHT_WINGBACK: { name: 'RWB Player' },
    LEFT_MIDFIELDER: { name: 'LM Player' },
    CENTRE_MIDFIELDER: [{ name: 'CM1' }, { name: 'CM2' }],
    RIGHT_MIDFIELDER: { name: 'RM Player' },
    LEFT_WINGER: { name: 'LW Player' },
    OFFENSIVE_MIDFIELDER: [{ name: 'OM1' }, { name: 'OM2' }],
    RIGHT_WINGER: { name: 'RW Player' },
    FORWARD: [{ name: 'F1' }, { name: 'F2' }]
};

describe('LineupPlayerView', () => {
    const renderWithProvider = (lineup: Lineup) => {
        return render(
            <LineupProvider>
                <LineupPlayerView lineup={lineup} />
            </LineupProvider>
        );
    };

    it('renders all lineup positions with buttons', () => {
        renderWithProvider(mockLineup);

        const buttons = screen.getAllByRole('button');
        expect(buttons.length).toBeGreaterThan(0);

        // Ensure that buttons have some text
        buttons.forEach(button => {
            expect(button.textContent?.trim().length).toBeGreaterThan(0);
        });
    });

    it('renders correct number of buttons for array positions', () => {
        renderWithProvider(mockLineup);

        // CENTRE_BACK has 2 players
        const cbButtons = screen.getAllByText(/CB/);
        expect(cbButtons.length).toBe(2);

        // DEFENSIVE_MIDFIELDER has 2 players
        const dmButtons = screen.getAllByText(/DM/);
        expect(dmButtons.length).toBe(2);

        // FORWARD has 2 players
        const fButtons = screen.getAllByText(/F/);
        expect(fButtons.length).toBe(2);
    });
});
