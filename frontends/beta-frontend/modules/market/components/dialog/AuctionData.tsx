import { Box } from '@mui/material'
import React from 'react'
import AuctionColumnText from './AuctionColumnText'
import { formatDateToDDMMYY } from '@/shared/utils/DateUtils'
import { AuctionResponse } from 'modules/market/types/responses'

interface AuctionDataProps {
    auction: AuctionResponse
}

const AuctionData: React.FC<AuctionDataProps> = ({ auction }) => {
    return (
        <Box
            sx={{
                mb: 1,
                display: 'flex',
                justifyContent: 'space-between',
                width: '100%',
            }}>
            <AuctionColumnText
                title="Average Bid"
                value={`${auction?.averageBid} $`}
            />
            <AuctionColumnText title="Total Bids" value={`${auction?.bidders}`} />
            <AuctionColumnText
                title="Auction Date"
                value={`${formatDateToDDMMYY(auction?.endedAt)}`}
            />
            <AuctionColumnText
                title="My bid"
                value={(auction?.bid || 0) + " $"}
            />
        </Box>
    )
}

export default AuctionData