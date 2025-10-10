import { render, screen, fireEvent } from '@testing-library/react'
import AuctionBidForm from '../AuctionBidForm'
import { useSession } from 'next-auth/react'
import { useMarketApi } from 'modules/market/hooks/useMarketApi'

jest.mock('next-auth/react')
jest.mock('modules/market/hooks/useMarketApi')

describe('AuctionBidForm', () => {
    const mockUpdateAuction = jest.fn()

    beforeEach(() => {
        jest.clearAllMocks()

            ; (useSession as jest.Mock).mockReturnValue({
                data: {
                    user: { teamId: '123' },
                    accessToken: 'mock-token',
                },
                status: 'authenticated',
            })

            ; (useMarketApi as jest.Mock).mockReturnValue({
                handlePlaceBid: mockUpdateAuction,
            })
    })

    it('renders input and button initially', () => {
        render(<AuctionBidForm auctionId="1" />)

        expect(screen.getByPlaceholderText('Your bid here...')).toBeInTheDocument()
        expect(screen.getByText('PLACE MY BID')).toBeInTheDocument()
    })

    it('lets user type into input', () => {
        render(<AuctionBidForm auctionId="1" />)

        const input = screen.getByPlaceholderText('Your bid here...') as HTMLInputElement
        fireEvent.change(input, { target: { value: '2500' } })

        expect(input.value).toBe('2500')
    })

    it('shows confirmation text after clicking PLACE MY BID', () => {
        render(<AuctionBidForm auctionId="1" />)

        const input = screen.getByPlaceholderText('Your bid here...') as HTMLInputElement
        fireEvent.change(input, { target: { value: '1500' } })

        fireEvent.click(screen.getByText('PLACE MY BID'))

        expect(screen.getByText('Your Bid')).toBeInTheDocument()
        expect(screen.getByText('1500 $')).toBeInTheDocument()
        expect(screen.getByText('YES, I AM SURE')).toBeInTheDocument()
    })

    it('calls updateAuction with correct bid on confirmation', () => {
        render(<AuctionBidForm auctionId="1" />)

        const input = screen.getByPlaceholderText('Your bid here...') as HTMLInputElement
        fireEvent.change(input, { target: { value: '1800' } })

        fireEvent.click(screen.getByText('PLACE MY BID'))
        fireEvent.click(screen.getByText('YES, I AM SURE'))

        expect(mockUpdateAuction).toHaveBeenCalledWith("1", { "amount": 1800 })
    })
})
