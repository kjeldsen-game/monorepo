import { useMediaQuery } from '@mui/material';
import { render, screen } from '@testing-library/react';
import React from 'react';
import LineupView from '../LineupView';
import { ThemeProvider } from '@mui/material/styles';
import { theme } from '@/libs/material/theme';
import { Player } from '@/shared/models/player/Player';

jest.mock('modules/player/hooks/logic/useLineupBuilder', () => ({
    useLineupBuilder: () => ({
        lineup: {
            GOALKEEPER: { name: 'GK Player' }
        },
        bench: [
            { name: 'Bench1' }, { name: 'Bench2' }
        ]
    })
}));

jest.mock('../LineupButton', () => (props: any) => (
    <div data-testid="lineup-button">{props.player?.name || 'Empty'}</div>
));


describe('LineupView', () => {
    const handleEdit = jest.fn();
    const mockPlayer: Player = { name: 'Active Player' } as Player;

    beforeEach(() => {
        (useMediaQuery as jest.Mock).mockReset();
    });

    it('renders lineup view for large screens', () => {
        (useMediaQuery as jest.Mock).mockReturnValue(false);

        render(
            <ThemeProvider theme={theme}>
                <LineupView
                    players={[]}
                    edit={true}
                    activePlayer={mockPlayer}
                    handleEdit={handleEdit}
                />
            </ThemeProvider>
        );

        expect(screen.getAllByTestId('lineup-button').length).toBeGreaterThan(0);
    });
});
