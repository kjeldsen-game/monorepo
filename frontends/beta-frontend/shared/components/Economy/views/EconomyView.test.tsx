import { render, screen, fireEvent } from '@testing-library/react';
import EconomyView from './EconomyView';

jest.mock('./TransactionsTabView', () => () => <div>TransactionsTabView Content</div>);
jest.mock('./GeneralEconomyTabView', () => () => <div>GeneralEconomyTabView Content</div>);
jest.mock('./WagesTabView', () => () => <div>WagesTabView Content</div>);

const mockEconomy = {
    transactions: [],
    billboardDeal: {},
    prices: {},
    balance: 0,
    sponsor: {},
    playerWages: [],
};

describe('EconomyView', () => {
    it('renders loading spinner when economy is undefined', () => {
        render(<EconomyView economy={undefined} />);
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });

    it('renders all tabs and default tab content', () => {
        render(<EconomyView economy={mockEconomy} />);

        expect(screen.getByRole('tab', { name: /transactions/i })).toBeInTheDocument();
        expect(screen.getByRole('tab', { name: /adjustments/i })).toBeInTheDocument();
        expect(screen.getByRole('tab', { name: /wages/i })).toBeInTheDocument();

        expect(screen.getByText(/TransactionsTabView Content/i)).toBeInTheDocument();
    });

    it('switches to Adjustments tab and shows correct placeholder', () => {
        render(<EconomyView economy={mockEconomy} />);
        fireEvent.click(screen.getByRole('tab', { name: /adjustments/i }));

        expect(screen.getByText(/GeneralEconomyTabView Content/i)).toBeInTheDocument();
    });

    it('switches to Wages tab and shows correct placeholder', () => {
        render(<EconomyView economy={mockEconomy} />);
        fireEvent.click(screen.getByRole('tab', { name: /wages/i }));

        expect(screen.getByText(/WagesTabView Content/i)).toBeInTheDocument();
    });

    it('renders dashboard link', () => {
        render(<EconomyView economy={mockEconomy} />);
        expect(screen.getByText(/Back to Dashboard/i)).toBeInTheDocument();
    });
});
