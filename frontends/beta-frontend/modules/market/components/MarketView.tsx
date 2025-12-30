import { Box, useMediaQuery } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { theme } from '@/libs/material/theme';
import MarketFilter from './filter/MarketFilter';
import AuctionDialog from './dialog/AuctionDialog';
import { MarketColumns } from './columns/MarketColumns';
import { AuctionResponse } from '../types/responses';
import Grid from '@/shared/components/Grid/Grid';
import NoDataError from '@/shared/components/Grid/no-data-error/NoDataError';
import { GavelOutlined } from '@mui/icons-material';
import { useTabManager } from '@/shared/hooks/useTabManager';
import { useModalManager } from '@/shared/hooks/useModalManager';
import CustomTabs from '@/shared/components/Tabs/CustomTabs';
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import MarketHistoryTab from './tabs/MarketHistoryTab';
import MarketTab from './tabs/MarketTab';

interface MarketProps {

}

const MarketView: React.FC<MarketProps> = ({

}: MarketProps) => {
  const { selectedTab, handleTabChange } = useTabManager();
  // const { setOpen, handleCloseModal, open } = useModalManager();
  const [activeAuction, setActiveAuction] = useState<AuctionResponse | undefined>(
    undefined,
  );
  const [open, setOpen] = useState<boolean>(false);



  const handleCloseModal = () => {
    setOpen(false);
  };

  const handleRowButtonClick = (auction: AuctionResponse) => {
    setActiveAuction(auction);
    setAuction(auction.id);
    setOpen(true);
  };

  const isXs = useMediaQuery(theme.breakpoints.down('sm'));
  const memoizedColumns = useMemo(() => MarketColumns(handleRowButtonClick, isXs), [isXs]);

  return (
    <Box sx={{ width: '100%', background: 'white' }} padding={1} borderRadius={2} boxShadow={1}>
      <Box sx={{ width: '100%' }}>
        <CustomTabs sx={{ paddingBottom: 1 }} handleChange={handleTabChange}
          selectedTab={selectedTab} tabs={["Live Auction", "Auction History"]} />
        <Box>
          <CustomTabPanel value={selectedTab} index={0}>
            <MarketTab />
          </CustomTabPanel>
          <CustomTabPanel value={selectedTab} index={1}>
            <MarketHistoryTab />
          </CustomTabPanel>
        </Box>
      </Box>
    </Box>


  );
};

export default MarketView;
