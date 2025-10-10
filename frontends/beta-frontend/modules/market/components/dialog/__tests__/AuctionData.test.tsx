import { render, screen } from '@testing-library/react'
import AuctionData from '../AuctionData'
import { formatDateToDDMMYY } from '@/shared/utils/DateUtils'

describe('AuctionData', () => {
    const mockAuction: any = {
        averageBid: 1500,
        bidders: 10,
        endedAt: '2025-07-01T12:00:00Z',
        bid: 1200,
    }

    it('renders the auction fields correctly', () => {
        render(<AuctionData auction={mockAuction} />)

        expect(screen.getByText('Average Bid')).toBeInTheDocument()
        expect(screen.getByText('1500 $')).toBeInTheDocument()

        expect(screen.getByText('Total Bids')).toBeInTheDocument()
        expect(screen.getByText('10')).toBeInTheDocument()

        expect(screen.getByText('Auction Date')).toBeInTheDocument()
        expect(screen.getByText(formatDateToDDMMYY(mockAuction.endedAt))).toBeInTheDocument()

    })
})
