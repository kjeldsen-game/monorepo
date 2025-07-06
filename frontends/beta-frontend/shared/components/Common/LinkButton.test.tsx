import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import LinkButton from './LinkButton';
import { ThemeProvider, createTheme } from '@mui/material/styles';

const renderWithTheme = (ui: React.ReactElement) => {
    const theme = createTheme();
    return render(<ThemeProvider theme={theme}>{ui}</ThemeProvider>);
};

describe('LinkButton', () => {
    it('renders the children text', () => {
        renderWithTheme(<LinkButton link="/test">Click Me</LinkButton>);
        expect(screen.getByText('Click Me')).toBeInTheDocument();
    });

    it('renders with outlined variant by default', () => {
        renderWithTheme(<LinkButton link="/outlined">Outlined</LinkButton>);
        const button = screen.getByText('Outlined');
        expect(button).toHaveStyle('border: 1px solid #FF3F84');
    });

    it('renders with contained variant when specified', () => {
        renderWithTheme(
            <LinkButton link="/contained" variant="contained">Contained</LinkButton>
        );
        const button = screen.getByText('Contained');
        expect(button).toHaveStyle('background-color: white');
    });

    it('applies custom sx styles', () => {
        renderWithTheme(
            <LinkButton link="/custom" sx={{ fontSize: '20px' }}>Styled</LinkButton>
        );
        const button = screen.getByText('Styled');
        expect(button).toHaveStyle('font-size: 20px');
    });

    it('renders link with correct href', () => {
        renderWithTheme(<LinkButton link="/route">Go</LinkButton>);
        const anchor = screen.getByRole('link');
        expect(anchor).toHaveAttribute('href', '/route');
    });
});
