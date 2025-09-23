import { render, screen, fireEvent } from '@testing-library/react';
import SellPlayerConfirmationDialog from '../SellPlayerConfirmationDialog';
import { usePlayerApi } from 'modules/player/hooks/api/usePlayerApi';

// Mock dependencies
jest.mock('modules/player/hooks/api/usePlayerApi');
jest.mock('@/shared/components/Common/CustomButton', () => (props: any) => (
    <button {...props}>{props.children}</button>
));
jest.mock('@/shared/components/Common/CloseButton', () => (props: any) => (
    <button data-testid="close-button" onClick={props.handleCloseModal} />
));
jest.mock('@mui/icons-material/LocalAtm', () => () => <div data-testid="atm-icon" />);

describe('SellPlayerConfirmationDialog', () => {
    const mockHandleClose = jest.fn();
    const mockHandleSellPlayerRequest = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
        // @ts-ignore
        usePlayerApi.mockReturnValue({
            handleSellPlayerRequest: mockHandleSellPlayerRequest,
        });
    });

    it('renders dialog when open', () => {
        render(
            <SellPlayerConfirmationDialog open={true} handleClose={mockHandleClose} playerId="player-1" />
        );

        expect(screen.getByText(/Are you sure that you want to sell player/i)).toBeInTheDocument();
        expect(screen.getByTestId('atm-icon')).toBeInTheDocument();
        expect(screen.getByTestId('sell-button')).toBeInTheDocument();
        expect(screen.getByTestId('close-button')).toBeInTheDocument();
    });

    it('calls handleSellPlayerRequest and handleClose when "Sell Player" button is clicked', () => {
        render(
            <SellPlayerConfirmationDialog open={true} handleClose={mockHandleClose} playerId="player-1" />
        );

        const sellButton = screen.getByTestId('sell-button');
        fireEvent.click(sellButton);

        expect(mockHandleSellPlayerRequest).toHaveBeenCalledTimes(1);
        expect(mockHandleClose).toHaveBeenCalledTimes(1);
    });

    it('calls handleClose when CloseButton is clicked', () => {
        render(
            <SellPlayerConfirmationDialog open={true} handleClose={mockHandleClose} playerId="player-1" />
        );

        const closeButton = screen.getByTestId('close-button');
        fireEvent.click(closeButton);

        expect(mockHandleClose).toHaveBeenCalledTimes(1);
    });

    it('does not render dialog content when open is false', () => {
        render(
            <SellPlayerConfirmationDialog open={false} handleClose={mockHandleClose} playerId="player-1" />
        );

        expect(screen.queryByText('Sell Player')).not.toBeInTheDocument();
    });
});
