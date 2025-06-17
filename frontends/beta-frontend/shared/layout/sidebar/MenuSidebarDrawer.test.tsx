import React from 'react';
import { render, screen } from '@testing-library/react';
import MenuSidebarDrawer from './MenuSidebarDrawer';
import '@testing-library/jest-dom';
import { useRouter } from 'next/router';
import { ThemeProvider, createTheme } from '@mui/material/styles';

// Mock MUI icons
jest.mock('@mui/icons-material/Inbox', () => () => <div data-testid="icon-inbox" />);
jest.mock('@mui/icons-material/Mail', () => () => <div data-testid="icon-mail" />);
jest.mock('@mui/icons-material/EmojiEvents', () => () => <div data-testid="icon-trophy" />);
jest.mock('@mui/icons-material/LocalGroceryStore', () => () => <div data-testid="icon-market" />);
jest.mock('@mui/icons-material/Groups2', () => () => <div data-testid="icon-team" />);
jest.mock('@mui/icons-material/FitnessCenter', () => () => <div data-testid="icon-training" />);
jest.mock('@mui/icons-material/CalendarMonth', () => () => <div data-testid="icon-league" />);
jest.mock('@mui/icons-material/Terminal', () => () => <div data-testid="icon-simulator" />);

// Mock Next.js router
jest.mock('next/router', () => ({
    useRouter: jest.fn(),
}));

const renderWithTheme = (component: React.ReactElement) => {
    const theme = createTheme();
    return render(<ThemeProvider theme={theme}>{component}</ThemeProvider>);
};

describe('MenuSidebarDrawer parameterized routing tests', () => {
    test.each([
        ['/dashboard', 'Dashboard'],
        ['/team', 'Team'],
        ['/challenge', 'Challenge'],
        ['/challenge/1', 'Challenge'],
        ['/league', 'League'],
        ['/simulator', 'Simulator'],
    ])('should highlight "%s" path and select "%s" menu item', (pathname, expectedSelectedItem) => {
        (useRouter as jest.Mock).mockReturnValue({ pathname });

        renderWithTheme(<MenuSidebarDrawer />);

        const item = screen.getByText(expectedSelectedItem).closest('li');

        expect(item).toHaveStyle('border-left: 8px solid #FF3F84');
        expect(item).toHaveStyle('background-color: white');
        expect(item).toHaveStyle('color: #FF3F84');
    });
});
