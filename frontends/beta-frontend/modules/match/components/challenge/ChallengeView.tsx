import CustomTabs from '@/shared/components/CustomTabs'
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel'
import { Alert, Tab } from '@mui/material'
import { Box } from '@mui/system'
import React, { useState } from 'react'
import GeneralTabView from './tabs/GeneralTabView'
import PastChallengesTabView from './tabs/PastChallengesTabView'
import AcceptedChallengesTabView from './tabs/AcceptedChallengesTabView'
import DashboardLink from '@/shared/components/DashboardLink'

const ChallengeView = () => {

    const [selectedTab, setSelectedTab] = useState(0);

    const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <DashboardLink children={'Back to Dashboard'} />
            <Alert sx={{ mb: '16px' }} severity="warning">

            </Alert>
            <Box>
                <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange} >
                    <Tab label="General" />
                    <Tab label="Accepted Challenges" />
                    <Tab label="Past Challenges" />
                </CustomTabs>
            </Box>
            <CustomTabPanel value={selectedTab} index={0}>
                <GeneralTabView />
            </CustomTabPanel>
            <CustomTabPanel value={selectedTab} index={1}>
                <AcceptedChallengesTabView />
            </CustomTabPanel>
            <CustomTabPanel value={selectedTab} index={2}>
                <PastChallengesTabView />
            </CustomTabPanel>
        </Box>
    )
}

export default ChallengeView