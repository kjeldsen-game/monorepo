import CustomTabs from '@/shared/components/CustomTabs'
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel'
import { Alert, Tab, Typography } from '@mui/material'
import { Box } from '@mui/system'
import React, { useState } from 'react'
import GeneralTabView from './tabs/GeneralTabView'
import PastChallengesTabView from './tabs/PastChallengesTabView'
import AcceptedChallengesTabView from './tabs/AcceptedChallengesTabView'
import DashboardLink from '@/shared/components/DashboardLink'
import PreAlphaAlert from '@/shared/components/PreAlphaAlert'

const ChallengeView = () => {

    const [selectedTab, setSelectedTab] = useState(0);

    const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    return (
        <>
            {/* <DashboardLink children={'Back to Dashboard'} /> */}


            <PreAlphaAlert />

            {/* <Alert sx={{
                borderTopLeftRadius: '0px',
                borderBottomLeftRadius: '0px',
                mb: '16px', borderLeft: '8px solid #FF3F84', color: '#FF3F84',
                backgroundColor: '#ffe0ecff',
                '& .MuiAlert-icon': {
                    color: '#FF3F84',
                },
            }} severity="warning">
                <Typography fontWeight={'bold'}>
                    Beta Testing
                </Typography>
                <Typography variant='body1'>
                    Module is in the beta testing.
                </Typography>
            </Alert> */}
            <Box sx={{ width: '100%', background: 'white' }} padding={2} borderRadius={2} boxShadow={1} >
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
        </>
    )
}

export default ChallengeView