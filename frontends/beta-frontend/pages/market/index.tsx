import type { NextPage } from 'next';
import { Box } from '@mui/material';
import MarketView from '@/shared/components/Market/MarketView';
import { useSession } from 'next-auth/react';
import { useEffect, useState } from 'react';
import { useAuctionRepository } from '../api/market/useAuctionRepository';

interface MarketProps {}

const Market: NextPage<MarketProps> = ({}) => {
  const [filter, setFilter] = useState<string>('');
  const [auction, setAuction] = useState<string | undefined>(undefined);
  const { data: userData } = useSession({ required: true });

  const { auctions, updateAuction, refetch } = useAuctionRepository(
    auction,
    userData?.accessToken,
    filter,
  );

  useEffect(() => {
    refetch();
  }, [filter]);

  const handleSetFilter = (filterInput: string) => {
    setFilter(filterInput);
  };

  const handleAuction = (auctionId: string) => {
    setAuction(auctionId);
  };

  return (
    <>
      <Box>
        <Box
          sx={{
            display: 'flex',
            marginBottom: '2rem',
            alignItems: 'center',
          }}>
          <MarketView
            setAuction={handleAuction}
            updateAuction={updateAuction}
            refetch={refetch}
            auctions={auctions}
            setFilter={handleSetFilter}
          />
        </Box>
      </Box>
    </>
  );
};

export default Market;
