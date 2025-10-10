import React from 'react'
import { render, screen } from '@testing-library/react'
import AuctionDialog from '../AuctionDialog'
import { useSession } from 'next-auth/react'

jest.mock('../AuctionData', () => () => <div>AuctionDataComponent</div>)
jest.mock('../AuctionBidForm', () => ({ auctionId }: { auctionId: string }) => (
    <div>AuctionBidFormComponent - {auctionId}</div>
))

jest.mock('next-auth/react')

describe('AuctionDialog', () => {
    const mockUser = {
        user: { teamId: 'team-123' },
        accessToken: 'fake-token',
    }

    const baseAuction: any = {
        id: 'auction-1',
        teamId: 'team-123',
        averageBid: 1000,
        bidders: 5,
        endedAt: new Date().toDateString,
        bid: 500,
    }

    beforeEach(() => {
        jest.clearAllMocks()
            ; (useSession as jest.Mock).mockReturnValue({
                data: mockUser,
                status: 'authenticated',
            })
    })

    it('renders the dialog with title and AuctionData', () => {
        render(<AuctionDialog open={true} handleClose={jest.fn()} auction={baseAuction} />)

        expect(screen.getByText('Bid for player')).toBeInTheDocument()
        expect(screen.getByText('AuctionDataComponent')).toBeInTheDocument()
    })

    it('shows owner warning when user owns the player', () => {
        render(<AuctionDialog open={true} handleClose={jest.fn()} auction={baseAuction} />)

        expect(screen.getByText('You are the owner of this player')).toBeInTheDocument()
        expect(screen.queryByText('AuctionBidFormComponent')).not.toBeInTheDocument()
    })

    it('shows AuctionBidForm when user is not the owner', () => {
        const auctionNotOwned = { ...baseAuction, teamId: 'another-team' }

        render(<AuctionDialog open={true} handleClose={jest.fn()} auction={auctionNotOwned} />)

        expect(screen.queryByText('You are the owner of this player')).not.toBeInTheDocument()
        expect(screen.getByText('AuctionBidFormComponent - auction-1')).toBeInTheDocument()
    })

})
