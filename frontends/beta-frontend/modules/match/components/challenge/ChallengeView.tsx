import CustomTabs from '@/shared/components/Tabs/CustomTabs'
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel'
import { Box } from '@mui/system'
import React, { useState } from 'react'
import GeneralTabView from './tabs/GeneralTabView'
import PastChallengesTabView from './tabs/PastChallengesTabView'
import AcceptedChallengesTabView from './tabs/AcceptedChallengesTabView'

const ChallengeView = () => {

    const [selectedTab, setSelectedTab] = useState(0);

    const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    return (
        <>
            <Box sx={{ width: '100%', background: 'white' }} padding={1} borderRadius={2} boxShadow={1} >
                <Box>
                    <CustomTabs sx={{ paddingBottom: 1 }} tabs={["General", "Accepted Challenges", "Past Challenges"]}
                        selectedTab={selectedTab} handleChange={handleTabChange} />
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