import { render, screen } from '@testing-library/react';
import EconomyCard from '../EconomyCard';
import { createTheme, ThemeProvider } from '@mui/material';

const theme = createTheme();

describe('EconomyCard', () => {
    it('renders children inside the card', () => {
        render(<EconomyCard>Test content</EconomyCard>);
        expect(screen.getByText('Test content')).toBeInTheDocument();
    });

    it('applies custom sx styles passed as props', () => {
        const customSx = { backgroundColor: 'red' };
        const { container } = render(
            <EconomyCard sx={customSx}>Styled</EconomyCard>,
        );

        const card = container.querySelector('.MuiCard-root');
        expect(card).toHaveStyle('background-color: red');
    });

    it('applies default styles and layout', () => {
        render(
            <ThemeProvider theme={theme}>
                <EconomyCard>Card</EconomyCard>
            </ThemeProvider>,
        );

        const grid = screen.getByTestId('economy-card-grid');
        const card = screen.getByTestId('economy-card-card');

        expect(grid).toBeInTheDocument();
        expect(card).toBeInTheDocument();

        expect(card).toHaveStyle('min-height: 500px');
        expect(card).toHaveStyle('text-align: center');
    });
});
