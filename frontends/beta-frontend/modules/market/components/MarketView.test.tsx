import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import MarketView from './MarketView';
import { AuctionMarket } from '@/shared/models/market/Auction';
import { mockPlayers } from '__mocks__/player';

jest.mock('./dialog/AuctionDialog', () => () => <div data-testid="auction-dialog" />);
jest.mock('./filter/MarketFilter', () => ({ setFilter }: any) => (
    <button onClick={() => setFilter('TEST_FILTER')}>Apply Filter</button>
));
jest.mock('@/shared/components/Grid/Grid', () => (props: any) => (
    <div data-testid="grid">
        {props.rows.map((row: any) => (
            <button key={row.id} onClick={() => props.columns[0].renderCell({ row })}>
                Open Auction {row.id}
            </button>
        ))}
    </div>
));

const mockAuctions = [
    {
        id: '1',
        averageBid: 10,
        bidders: 2,
        endedAt: new Date().toISOString(),
        bid: 5,
        teamId: 'team-1',
        player: mockPlayers[0]
    },
];

describe('MarketView', () => {

    it('renders grid and auctions', () => {
        render(
            <MarketView
                auctions={mockAuctions}
                setFilter={jest.fn()}
                setAuction={jest.fn()}
            />
        );

        expect(screen.getByTestId('grid')).toBeInTheDocument();
        expect(screen.getByText('Open Auction 1')).toBeInTheDocument();
    });

    it('opens dialog when auction row is clicked', async () => {
        render(
            <MarketView
                auctions={mockAuctions}
                setFilter={jest.fn()}
                setAuction={jest.fn()}
            />
        );

        fireEvent.click(screen.getByText('Open Auction 1'));

        await waitFor(() => {
            expect(screen.getByTestId('auction-dialog')).toBeInTheDocument();
        });
    });

    it('calls setFilter when filter is applied', () => {
        const setFilterMock = jest.fn();

        render(
            <MarketView
                auctions={mockAuctions}
                setFilter={setFilterMock}
                setAuction={jest.fn()}
            />
        );

        fireEvent.click(screen.getByText('Apply Filter'));
        expect(setFilterMock).toHaveBeenCalledWith('TEST_FILTER');
    });
});
