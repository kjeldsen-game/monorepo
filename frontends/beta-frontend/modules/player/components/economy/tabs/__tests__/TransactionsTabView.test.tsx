import { render, screen } from '@testing-library/react';
import TransactionsTabView from '../TransactionsTabView';
import { mockTransactions } from 'modules/player/__fixtures__/economy.fixture';

describe('TransactionsTabView', () => {
    it('renders rows with correct class based on context', () => {
        render(<TransactionsTabView transactions={mockTransactions} isLoading={false} />);

        const rows = screen.getAllByRole('row').slice(1);

        expect(rows.length).toBe(mockTransactions.length);

        expect(rows[10]).toHaveClass('super-app-theme--TotalIncome');
        expect(rows[11]).toHaveClass('super-app-theme--TotalOutcome');
        expect(rows[12]).toHaveClass('super-app-theme--TotalBalance');
    });
});
