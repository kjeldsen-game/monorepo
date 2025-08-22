import { render, screen, fireEvent } from '@testing-library/react';
import BillboardCard from '../BillboardCard';

describe('BillboardCard', () => {
    const mockSetOpen = jest.fn();

    const mockDeal = {
        type: 'Super Ad',
        offer: 5000,
        startSeason: 3,
        endSeason: 4,
    };

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('renders billboard data when deal is provided', () => {
        render(<BillboardCard billboardDeal={mockDeal} setOpen={mockSetOpen} />);

        expect(screen.getByText('Billboard Deal')).toBeInTheDocument();
        expect(screen.getByText('Type:')).toBeInTheDocument();
        expect(screen.getByText('Super ad')).toBeInTheDocument();
        expect(screen.getByText('Offer:')).toBeInTheDocument();
        expect(screen.getByText(/5,000.00 \$?/)).toBeInTheDocument(); // with $ symbol
        expect(screen.getByText('Valid from Season:')).toBeInTheDocument();
        expect(screen.getByText('3')).toBeInTheDocument();
        expect(screen.queryByText('Choose Offer')).not.toBeInTheDocument();
    });

    it('shows warning when no deal is selected', () => {
        render(<BillboardCard billboardDeal={undefined} setOpen={mockSetOpen} />);

        expect(
            screen.getByText(/You don't have selected Billboard Deal/i)
        ).toBeInTheDocument();
        expect(screen.getByText('Choose Offer')).toBeInTheDocument();
    });

    it('calls setOpen when "Choose Offer" button is clicked', () => {
        render(<BillboardCard billboardDeal={undefined} setOpen={mockSetOpen} />);

        const button = screen.getByText('Choose Offer');
        fireEvent.click(button);

        expect(mockSetOpen).toHaveBeenCalledWith(true);
    });
});
