import React from 'react';
import { render, screen } from '@testing-library/react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import AuctionColumnText from './AuctionColumnText';

const renderWithTheme = (ui: React.ReactElement) => {
    const theme = createTheme();
    return render(<ThemeProvider theme={theme}>{ui}</ThemeProvider>);
};

describe('AuctionColumnText', () => {
    it('renders title and value correctly', () => {
        renderWithTheme(<AuctionColumnText title="Bid" value="€500" />);
        expect(screen.getByText('Bid')).toBeInTheDocument();
        expect(screen.getByText('€500')).toBeInTheDocument();
    });

    it('applies custom sx styles', () => {
        renderWithTheme(<AuctionColumnText title="Time" value="2h" sx={{ fontWeight: 700 }} />);
        const valueEl = screen.getByText('2h');
        expect(valueEl).toHaveStyle('font-weight: 700');
    });

    it('has correct default styles for outer Box', () => {
        const { container } = renderWithTheme(<AuctionColumnText title="Test" value="123" />);
        const outerBox = container.firstChild;
        expect(outerBox).toHaveStyle('flex: 1');
        expect(outerBox).toHaveStyle('text-align: center');
    });
});
