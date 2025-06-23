import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import SponsorDialogCard from './SponsorDialogCard';

describe('SponsorDialogCard', () => {
    const defaultProps = {
        mode: 'Aggressive',
        name: 'Aggressive',
        base: 150000,
        bonus: 30000,
        type: 'WEEKLY',
        handleButtonClick: jest.fn(),
    };

    it('renders all content correctly', () => {
        render(<SponsorDialogCard {...defaultProps} />);

        expect(screen.getByText(/Aggressive/i)).toBeInTheDocument();

        expect(
            screen.getByText((content, element) =>
                element?.textContent === 'Base Offer: 150,000 $'
            )
        ).toBeInTheDocument();

        expect(
            screen.getByText((content, element) =>
                element?.textContent === 'Win bonus: 30,000 $'
            )
        ).toBeInTheDocument();

        expect(
            screen.getByRole('button', { name: /Choose offer/i })
        ).toBeInTheDocument();
    });


    it('calls handleButtonClick with correct args when button is clicked', () => {
        render(<SponsorDialogCard {...defaultProps} />);

        const button = screen.getByRole('button', { name: /Choose offer/i });
        fireEvent.click(button);

        expect(defaultProps.handleButtonClick).toHaveBeenCalledTimes(1);
        expect(defaultProps.handleButtonClick).toHaveBeenCalledWith(
            defaultProps.mode,
            defaultProps.type
        );
    });
});
