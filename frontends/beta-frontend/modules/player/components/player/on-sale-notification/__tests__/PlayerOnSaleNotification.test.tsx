import { render, screen, fireEvent } from '@testing-library/react';
import PlayerOnSaleNotification from '../PlayerOnSaleNotification';
import { useMarketApi } from 'modules/market/hooks/useMarketApi';
import { useModalManager } from '@/shared/hooks/useModalManager';

jest.mock('modules/market/hooks/useMarketApi');
jest.mock('@/shared/hooks/useModalManager');
jest.mock('modules/market/components/dialog/AuctionDialog', () => (props: any) => (
    <div data-testid="auction-dialog">{props.open ? 'open' : 'closed'}</div>
));
jest.mock('@/shared/components/Common/CustomIconButton', () => (props: any) => (
    <button {...props}>{props.children}</button>
));
jest.mock('@mui/icons-material/LocalAtmOutlined', () => () => <div data-testid="atm-icon" />);

describe('PlayerOnSaleNotification', () => {
    const mockSetOpen = jest.fn();
    const mockHandleCloseModal = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
        // @ts-ignore
        useModalManager.mockReturnValue({
            open: false,
            setOpen: mockSetOpen,
            handleCloseModal: mockHandleCloseModal,
        });
    });

    it('renders notification when data has items', () => {
        // @ts-ignore
        useMarketApi.mockReturnValue({
            data: [{ id: 'auction-1' }],
            error: null,
            isLoading: false,
            handlePlaceBid: jest.fn(),
        });

        render(<PlayerOnSaleNotification playerId="player-1" />);

        expect(screen.getByText(/THIS PLAYER IS FOR SALE/i)).toBeInTheDocument();
        expect(screen.getByTestId('auction-dialog')).toBeInTheDocument();
    });

    it('does not render notification when data is empty', () => {
        // @ts-ignore
        useMarketApi.mockReturnValue({
            data: [],
            error: null,
            isLoading: false,
            handlePlaceBid: jest.fn(),
        });

        render(<PlayerOnSaleNotification playerId="player-1" />);

        expect(screen.queryByText(/THIS PLAYER IS FOR SALE/i)).not.toBeVisible();
    });

    it('calls setOpen(true) when button is clicked', () => {
        // @ts-ignore
        useMarketApi.mockReturnValue({
            data: [{ id: 'auction-1' }],
            error: null,
            isLoading: false,
            handlePlaceBid: jest.fn(),
        });

        render(<PlayerOnSaleNotification playerId="player-1" />);

        const button = screen.getByRole('button');
        fireEvent.click(button);

        expect(mockSetOpen).toHaveBeenCalledWith(true);
    });
});
