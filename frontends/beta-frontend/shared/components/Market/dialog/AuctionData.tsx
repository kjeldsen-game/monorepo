import { Box } from '@mui/material'
import React from 'react'
import AuctionColumnText from './AuctionColumnText'
import { AuctionMarket } from '@/shared/models/market/Auction'
import { formatDateToDDMMYY } from '@/shared/utils/DateUtils'

interface AuctionDataProps {
    auction: AuctionMarket
}

const AuctionData: React.FC<AuctionDataProps> = ({ auction }) => {
    return (
        <Box
            sx={{
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