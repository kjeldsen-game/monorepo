import { render, screen, fireEvent } from '@testing-library/react';
import EconomyView from './EconomyView';

jest.mock('./tabs/TransactionsTabView', () => () => <div>TransactionsTabView Content</div>);
jest.mock('./tabs/GeneralEconomyTabView', () => () => <div>GeneralEconomyTabView Content</div>);
jest.mock('./tabs/WagesTabView', () => () => <div>WagesTabView Content</div>);

jest.mock('next-auth/react', () => ({
    ...jest.requireActual('next-auth/react'),
    useSession: jest.fn(() => ({
        data: { user: { teamId: '123' }, accessToken: 'token' },
        status: 'authenticated',
    })),
}));

jest.mock('modules/player/hooks/api/useEconomyApi', () => ({
    useEconomyApi: () => ({
        data: {
            transactions: [],
            billboardDeal: [],
            prices: [],
            balance: 0,
            sponsor: [],
            playerWages: [],
        },
        isLoading: false,
    }),
}));

describe('EconomyView', () => {
    it('renders all tabs and default tab content', () => {
        render(<EconomyView />);

        expect(screen.getByRole('tab', { name: /transactions/i })).toBeInTheDocument();
        expect(screen.getByRole('tab', { name: /sponsorship/i })).toBeInTheDocument();
        expect(screen.getByRole('tab', { name: /wages/i })).toBeInTheDocument();

        expect(screen.getByText(/TransactionsTabView Content/i)).toBeInTheDocument();
    });

    it('switches to Sponsorship tab and shows correct placeholder', () => {
        render(<EconomyView />);
        fireEvent.click(screen.getByRole('tab', { name: /sponsorship/i }));

        expect(screen.getByText(/GeneralEconomyTabView Content/i)).toBeInTheDocument();
    });

    it('switches to Wages tab and shows correct placeholder', () => {
        render(<EconomyView />);
        fireEvent.click(screen.getByRole('tab', { name: /wages/i }));

        expect(screen.getByText(/WagesTabView Content/i)).toBeInTheDocument();
    });
});
