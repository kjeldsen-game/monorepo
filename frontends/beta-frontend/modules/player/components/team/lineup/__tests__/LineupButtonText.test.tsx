import React from 'react';
import { render, screen } from '@testing-library/react';
import LineupButtonText from '../LineupButtonText';
import { Player } from '@/shared/models/player/Player';

// Mock MUI useMediaQuery
jest.mock('@mui/material', () => ({
    ...jest.requireActual('@mui/material'),
    useMediaQuery: jest.fn(),
}));

const mockUseMediaQuery = require('@mui/material').useMediaQuery as jest.Mock;

describe('LineupButtonText', () => {
    const mockPlayer: Player = { id: '1', name: 'Lionel Messi', position: 'FORWARD' } as Player;

    it('renders surname when player is provided', () => {
        mockUseMediaQuery.mockReturnValue(false);

        render(<LineupButtonText player={mockPlayer} />);

        expect(screen.getByText('Messi')).toBeInTheDocument();
    });

    it('applies correct styles for non-xs screens', () => {
        mockUseMediaQuery.mockReturnValue(false);

        render(<LineupButtonText player={mockPlayer} />);
        const div = screen.getByText('Messi');

        expect(div).toHaveStyle('padding-left: 4px');
        expect(div).not.toHaveStyle('padding-bottom: 4px');
    });

    it('applies correct styles for xs screens', () => {
        mockUseMediaQuery.mockReturnValue(true);

        render(<LineupButtonText player={mockPlayer} />);
        const div = screen.getByText('Messi');

        expect(div).toHaveStyle('padding-bottom: 0px');
        expect(div).not.toHaveStyle('padding-left: 4px');
    });
});
