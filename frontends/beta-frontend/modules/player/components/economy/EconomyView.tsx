import CustomTabs from '@/shared/components/Tabs/CustomTabs';
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import { Box, Tab, } from '@mui/material';
import { useState } from 'react';
import TransactionsTabView from './tabs/TransactionsTabView';
import GeneralEconomyTabView from './tabs/GeneralEconomyTabView';
import WagesTabView from './tabs/WagesTabView';
import { useEconomyApi } from 'modules/player/hooks/api/useEconomyApi';

interface EconomyViewProps {
}

const EconomyView: React.FC<EconomyViewProps> = ({
}: EconomyViewProps) => {
    const [selectedTab, setSelectedTab] = useState(0);

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    const { data, isLoading } = useEconomyApi();

    return (
        <>
            <Box sx={{ width: '100%', background: 'white' }} padding={2} borderRadius={2} boxShadow={1} >
                <Box>
                    <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
                        <Tab label="Transactions" />
                        <Tab label="Sponsorship" />
                        <Tab label="Wages" />
                    </CustomTabs>
                </Box>

                <Box>
                    <CustomTabPanel value={selectedTab} index={0}>
                        <TransactionsTabView
                            isLoading={isLoading}
                            transactions={data?.transactions}>
                        </TransactionsTabView>
                    </CustomTabPanel>
                    <CustomTabPanel value={selectedTab} index={1}>
                        <GeneralEconomyTabView
                            billboardDeal={data?.billboardDeal}
                            prices={data?.prices}
                            balance={data?.balance}
                            sponsors={data?.sponsor}>
                        </GeneralEconomyTabView>
                    </CustomTabPanel>
                    <CustomTabPanel value={selectedTab} index={2}>
                        <WagesTabView
                            playerTransactions={data?.playerWages}
                            isLoading={isLoading} />
                    </CustomTabPanel>
                </Box>
            </Box>
        </>
    );
};

export default EconomyView;
