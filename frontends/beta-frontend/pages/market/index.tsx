import type { NextPage } from 'next';
import { Box } from '@mui/material';
import MarketView from '@/shared/components/Market/MarketView';
import { useAllAuctionRepository } from '../api/market/useAllAuctionRepository';

interface MarketProsp {
    fallback: () => void;
}

const Market: NextPage<MarketProsp> = ({ fallback }) => {
    const { allAuctions, refetch } = useAllAuctionRepository();

    return (
        <>
            <Box>
                <Box
                    sx={{
                        display: 'flex',
                        marginBottom: '2rem',
                        alignItems: 'center',
                    }}>
                    <MarketView refetch={refetch} auctions={allAuctions} />
                </Box>
            </Box>
        </>
    );
};

export default Market;
