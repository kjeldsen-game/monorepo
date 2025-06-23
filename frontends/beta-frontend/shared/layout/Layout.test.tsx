import React from 'react';
import { render, screen } from '@testing-library/react';
import { Layout } from './Layout';

jest.mock('./header/Header', () => () => <div data-testid="header">Header</div>);
jest.mock('./header/Subheader', () => () => <div data-testid="subheader">Subheader</div>);
jest.mock('./header/HeaderDivider', () => () => <div data-testid="header-divider">HeaderDivider</div>);
jest.mock('./sidebar/MenuSidebar', () => {
    return {
        __esModule: true,
        default: ({ mobileOpen }: { mobileOpen: boolean }) => (
            <div data-testid="menu-sidebar">{mobileOpen ? 'Open' : 'Closed'}</div>
        ),
        DRAWER_WIDTH: 240,
    };
});
jest.mock('./Main', () => ({
    Main: ({ children }: { children: React.ReactNode }) => (
        <main data-testid="main">{children}</main>
    ),
}));

describe('Layout component', () => {
    it('renders all main layout components', () => {
        render(
            <Layout>
                <div>Page Content</div>
            </Layout>
        );

        expect(screen.getByTestId('header')).toBeInTheDocument();
        expect(screen.getByTestId('subheader')).toBeInTheDocument();
        expect(screen.getByTestId('header-divider')).toBeInTheDocument();
        expect(screen.getByTestId('menu-sidebar')).toBeInTheDocument();
        expect(screen.getByTestId('main')).toBeInTheDocument();

        expect(screen.getByText('Page Content')).toBeInTheDocument();
    });
});
