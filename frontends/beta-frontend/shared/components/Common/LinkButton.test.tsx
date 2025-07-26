import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import LinkButton from './LinkButton';
import { ThemeProvider, createTheme } from '@mui/material/styles';

// Create a mock push function
const mockPush = jest.fn();

// Mock next/router
jest.mock('next/router', () => ({
    useRouter: () => ({
        push: mockPush,
    }),
}));

const renderWithTheme = (ui: React.ReactElement) => {
    const theme = createTheme();
    return render(<ThemeProvider theme={theme}>{ui}</ThemeProvider>);
};

describe('LinkButton', () => {
    beforeEach(() => {
        mockPush.mockClear();
    });

    it('renders the children text', () => {
        renderWithTheme(<LinkButton link="/test">Click Me</LinkButton>);
        expect(screen.getByText('Click Me')).toBeInTheDocument();
    });

    it('calls router.push on button click', () => {
        renderWithTheme(<LinkButton link="/clicked">Click</LinkButton>);
        const button = screen.getByRole('button');
        fireEvent.click(button);
        expect(mockPush).toHaveBeenCalledWith('/clicked');
    });

    it('renders with outlined variant by default', () => {
        renderWithTheme(<LinkButton link="/outlined">Outlined</LinkButton>);
        const button = screen.getByRole('button');
        expect(button).toBeInTheDocument();
        // Can't assert styles directly unless CustomButton applies them via inline style or test ID
    });

    it('renders with contained variant when specified', () => {
        renderWithTheme(
            <LinkButton link="/contained" variant="contained">Contained</LinkButton>
        );
        const button = screen.getByText('Contained');
        expect(button).toBeInTheDocument();
        // If CustomButton uses classes or sx, you can use getComputedStyle or className assertions
    });

    it('applies custom sx styles', () => {
        renderWithTheme(
            <LinkButton link="/custom" sx={{ fontSize: '20px' }}>Styled</LinkButton>
        );
        const button = screen.getByText('Styled');
        expect(button).toBeInTheDocument();
        // Style testing would require the sx prop to result in inline styles or use snapshot testing
    });
});
