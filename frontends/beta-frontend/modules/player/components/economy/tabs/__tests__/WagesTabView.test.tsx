import { render, screen } from '@testing-library/react';
import WagesTabView from '../WagesTabView';
import { PlayerWage } from 'modules/player/types/Economy';

// Mock the Grid component
jest.mock('@/shared/components/Grid/Grid', () => (props: any) => {
    return (
        <div data-testid="mock-grid">
            {props.rows.map((row: any) => (
                <div key={row.player.id} data-testid="grid-row">
                    {row.player.name} - {row.transactionSummary.weekSummary}
                </div>
            ))}
        </div>
    );
});

describe('WagesTabView', () => {
    const mockPlayerTransactions: PlayerWage[] = [
        { player: { id: '1', name: 'Player 1' }, transactionSummary: { weekSummary: 1000, seasonSummary: 1000 } },
        { player: { id: '2', name: 'Player 2' }, transactionSummary: { weekSummary: 1500, seasonSummary: 1500 } },
    ];

    it('renders the Grid with player transactions', () => {
        render(<WagesTabView playerTransactions={mockPlayerTransactions} isLoading={false} />);

        expect(screen.getByTestId('mock-grid')).toBeInTheDocument();

        const rows = screen.getAllByTestId('grid-row');
        expect(rows.length).toBe(mockPlayerTransactions.length);
        expect(rows[0]).toHaveTextContent('Player 1 - 1000');
        expect(rows[1]).toHaveTextContent('Player 2 - 1500');
    });

    it('renders no rows when empty', () => {
        render(<WagesTabView playerTransactions={[]} isLoading={false} />);
        expect(screen.getByTestId('mock-grid')).toBeInTheDocument();
        expect(screen.queryAllByTestId('grid-row').length).toBe(0);
    });
});
