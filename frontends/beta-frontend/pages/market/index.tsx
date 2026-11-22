import type { NextPage } from 'next';
import { useState } from 'react';
import { useMarketApi } from 'modules/market/hooks/useMarketApi';
import MarketView from 'modules/market/components/MarketView';
import { getSession, useSession } from '@/shared/mocks/fakeNextAuth';

interface MarketProps { }

const Market: NextPage<MarketProps> = ({ }) => {

  return (
    <MarketView />
  );
};

export default Market;
