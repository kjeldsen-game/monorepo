import { render, screen } from '@testing-library/react';
import TransactionsTabView from './TransactionsTabView';

jest.mock('../../Grid/Grid', () => (props: any) => {
    const { rows, getRowClassName } = props;
    return (
        <div data-testid="mock-grid">
            {rows.map((row: any) => (
                <div
                    key={row.context}
                    data-testid="grid-row"
                    className={getRowClassName ? getRowClassName({ row }) : ''}
                >
                    {row.context}
                </div>
            ))}
        </div>
    );
});

describe('TransactionsTabView', () => {

    it.each([undefined, null])(
        'renders loading spinner if transactions is %s',
        (transactionsValue) => {
            render(<TransactionsTabView transactions={transactionsValue} />);
            expect(screen.getByRole('progressbar')).toBeInTheDocument();
        }
    );

    it('renders rows with correct class based on context', () => {
        const transactions = [
            { context: 'Total Income' },
            { context: 'Total Outcome' },
            { context: 'Total Balance' },
            { context: 'Other' },
        ];

        render(<TransactionsTabView transactions={transactions} />);

        const rows = screen.getAllByTestId('grid-row');

        expect(rows.length).toBe(4);

        expect(rows[0]).toHaveClass('super-app-theme--TotalIncome');
        expect(rows[1]).toHaveClass('super-app-theme--TotalOutcome');
        expect(rows[2]).toHaveClass('super-app-theme--TotalBalance');
        expect(rows[3]).not.toHaveClass(
            'super-app-theme--TotalIncome super-app-theme--TotalOutcome super-app-theme--TotalBalance'
        );
    });
});
