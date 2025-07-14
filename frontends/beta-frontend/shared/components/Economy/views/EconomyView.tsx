import { Alert, Box, CircularProgress, Tab } from '@mui/material';
import { useState } from 'react';
import WagesTabView from './WagesTabView';
import GeneralEconomyTabView from './GeneralEconomyTabView';
import TransactionsTabView from './TransactionsTabView';
import DashboardLink from '../../DashboardLink';
import CustomTabs from '../../CustomTabs';
import { CustomTabPanel } from '../../Tab/CustomTabPanel';

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
      <Alert sx={{ mb: '16px' }} severity="warning">
        Please note that the Economy module is still in the development and test process, so no all funcionalities
        could work 100% as well the bugs could occured. The funcions in the Adjusment Tab should be workable, you are able
        to sign billoard/sponsor update pricing with respecive notification/error pop up after operation.
      </Alert>
      <Box>
        <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
          <Tab label="Transactions" />
          <Tab label="Adjustments" />
          <Tab label="Wages" />
        </CustomTabs>
      </Box>

      <Box>
        <CustomTabPanel value={selectedTab} index={0}>
          <TransactionsTabView
            transactions={economy.transactions}></TransactionsTabView>
        </CustomTabPanel>
        <CustomTabPanel value={selectedTab} index={1}>
          <GeneralEconomyTabView
            billboardDeal={economy.billboardDeal}
            prices={economy.prices}
            balance={economy.balance}
            sponsors={economy.sponsor}></GeneralEconomyTabView>
        </CustomTabPanel>
        <CustomTabPanel value={selectedTab} index={2}>
          <WagesTabView playerTransactions={economy.playerWages}></WagesTabView>
        </CustomTabPanel>
      </Box>
    </Box>
  );
};

export default EconomyView;
