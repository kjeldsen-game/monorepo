import type { NextPage } from 'next';
import { useState } from 'react';
import { useMarketApi } from 'modules/market/hooks/useMarketApi';
import MarketView from 'modules/market/components/MarketView';

interface MarketProps { }

const Market: NextPage<MarketProps> = ({ }) => {
  const [filter, setFilter] = useState<string>('');
  const [auction, setAuction] = useState<string | undefined>(undefined);

  const { data: auctions } = useMarketApi(filter)

  const handleSetFilter = (filterInput: string) => {
    setFilter(filterInput);
  };

  const handleAuction = (auctionId: string) => {
    setAuction(auctionId);
  };

  return (
    <>
      <MarketView
        setAuction={handleAuction}
        auctions={auctions}
        setFilter={handleSetFilter}
      />
    </>
  );
};

export default Market;
