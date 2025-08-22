import React from 'react';
import { render, screen } from '@testing-library/react';
import LineupButton from '../LineupButton';
import { Player } from '@/shared/models/player/Player';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';

jest.mock('modules/player/contexts/LineupContext', () => ({
    useLineupEdit: () => ({
        handleEdit: jest.fn(),
        activePlayer: null,
        edit: false,
    }),
}));

describe('LineupButton', () => {
    const player: Player = {
        id: '1',
        name: 'John Doe',
        position: PlayerPosition.GOALKEEPER,
    } as unknown as Player;

    it('renders position label and player name when player exists', () => {
        render(<LineupButton player={player} />);
        expect(screen.getByText("GK")).toBeInTheDocument();
        expect(screen.getByText('Doe')).toBeInTheDocument();
    });

    it('renders placeholder and icon when player is missing', () => {
        render(<LineupButton position={PlayerPosition.CENTRE_BACK} />);
        expect(screen.getByText("CB")).toBeInTheDocument();
    });
});
