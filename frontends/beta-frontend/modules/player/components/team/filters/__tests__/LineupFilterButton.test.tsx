import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import LineupFilterButton from '../LineupFilterButton';

// Mock useMediaQuery from MUI
jest.mock('@mui/material', () => {
    const actual = jest.requireActual('@mui/material');
    return {
        ...actual,
        useMediaQuery: jest.fn(),
    };
});

import { useMediaQuery } from '@mui/material';
const mockedUseMediaQuery = useMediaQuery as jest.Mock;

describe('LineupFilterButton', () => {
    const mockHandleClick = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders full name when not xs (desktop)', () => {
        mockedUseMediaQuery.mockReturnValue(false); // not xs

        render(
            <LineupFilterButton
                name="Midfield"
                handleClick={mockHandleClick}
                active=""
            />
        );

        expect(screen.getByRole('button')).toHaveTextContent('MIDFIELD');

        fireEvent.click(screen.getByRole('button'));
        expect(mockHandleClick).toHaveBeenCalledWith('MIDFIELD');
    });

    it('renders substring (first 3 chars) when xs (mobile)', () => {
        mockedUseMediaQuery.mockReturnValue(true); // xs screen

        render(
            <LineupFilterButton
                name="Midfield"
                handleClick={mockHandleClick}
                active=""
            />
        );

        expect(screen.getByRole('button')).toHaveTextContent('MID');

        fireEvent.click(screen.getByRole('button'));
        expect(mockHandleClick).toHaveBeenCalledWith('MIDFIELD');
    });
});
