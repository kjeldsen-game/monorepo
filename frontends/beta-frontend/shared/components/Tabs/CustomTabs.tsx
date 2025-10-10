import { Box, SxProps, Tab, Tabs } from '@mui/material';
import React, { useState } from 'react';

interface CustomTabsProps {
    selectedTab: any;
    handleChange: any;
    sx?: SxProps;
    tabs: string[]
}

const CustomTabs: React.FC<CustomTabsProps> = ({
    selectedTab,
    tabs,
    handleChange,
    sx = {},
}: CustomTabsProps) => {
    return (
        <Box
            sx={{ display: 'flex', justifyContent: 'center', width: '100%', ...sx }}>
            <Tabs
                variant='scrollable'
                scrollButtons="auto"
                textColor="secondary"
                indicatorColor="secondary"
                value={selectedTab}
                onChange={handleChange}

                aria-label="scrollable auto tabs example"
                slotProps={{
                    indicator: {
                        style: {
                            backgroundColor: '#FF3F84',
                        }
                    }
                }}
                sx={{
                    borderBottom: '1px solid #f9f9f9d8',
                    '.Mui-selected': {
                        fontWeight: 'bold',
                    },
                }}>
                {tabs.map((tab) => {
                    return <Tab key={tab} label={tab.toUpperCase()} />
                })}
            </Tabs>
        </Box>
    );
};

export default CustomTabs;
