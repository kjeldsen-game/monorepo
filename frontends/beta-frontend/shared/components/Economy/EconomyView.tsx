import { Box, CircularProgress, Tab } from '@mui/material';
import { useState } from 'react';
import DashboardLink from '../DashboardLink';
import { CustomTabPanel } from '../Tab/CustomTabPanel';
import TransactionsTabView from './TransactionsTabView';
import CustomTabs from '../CustomTabs';
import WagesTabView from './WagesTabView';
import GeneralEconomyTabView from './GeneralEconomyTabView';

interface EconomyViewProps {
  economy: any;
}

const EconomyView: React.FC<EconomyViewProps> = ({
  economy,
}: EconomyViewProps) => {
  const [selectedTab, setSelectedTab] = useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  if (economy == undefined || economy == false) {
    return <CircularProgress></CircularProgress>;
  }

  return (
    <Box sx={{ width: '100%' }}>
      <DashboardLink children={'Back to Dashboard'} />

      <Box>
        <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
          <Tab label="General" />
          <Tab label="Transactions" />
          <Tab label="Wages" />
        </CustomTabs>
      </Box>

      <Box>
        <CustomTabPanel value={selectedTab} index={0}>
          <GeneralEconomyTabView
            billboardDeal={economy.billboardDeal}
            prices={economy.prices}
            balance={economy.balance}
            sponsors={economy.sponsor}></GeneralEconomyTabView>
        </CustomTabPanel>
        <CustomTabPanel value={selectedTab} index={1}>
          <TransactionsTabView
            transactions={economy.transactions}></TransactionsTabView>
        </CustomTabPanel>
        <CustomTabPanel value={selectedTab} index={2}>
          <WagesTabView playerTransactions={economy.playerWages}></WagesTabView>
        </CustomTabPanel>
      </Box>
    </Box>
  );
};

export default EconomyView;
