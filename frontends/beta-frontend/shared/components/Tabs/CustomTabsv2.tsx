import { Box } from '@mui/system'
import React from 'react'
import CustomTabs from '../CustomTabs'
import { useTabManager } from '@/shared/hooks/useTabManager'
import { Tab } from '@mui/material';

interface CustomTabsv2Props {
    handleTabChange: (event, tab: number) => void;
    selectedTab: number;
    tabs: string[];
}

const CustomTabsv2: React.FC<CustomTabsv2Props> = ({ tabs, selectedTab, handleTabChange }) => {

    return (
        <Box
            sx={{ display: 'flex', justifyContent: 'center', width: '100%' }}
            paddingY={2}>
            <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange}>
                {tabs.map((tab) => {
                    return <Tab label={tab.toUpperCase()} />
                })}
            </CustomTabs>
        </Box>
    )
}

export default CustomTabsv2