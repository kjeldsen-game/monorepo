import { Box, SxProps, Tabs } from '@mui/material';
import React, { useState } from 'react';

interface CustomTabsProps {
    children: React.ReactNode;
    selectedTab: any;
    handleChange: any;
    sx?: SxProps;
}

const CustomTabs: React.FC<CustomTabsProps> = ({
    children,
    selectedTab,
    handleChange,
    sx = {},
}: CustomTabsProps) => {
    return (
        <Tabs
            variant='scrollable'
            scrollButtons="auto"
            textColor="secondary"
            indicatorColor="secondary"
            value={selectedTab}
            onChange={handleChange}

            aria-label="scrollable auto tabs example"
            TabIndicatorProps={{
                style: {
                    backgroundColor: '#FF3F84',
                },
            }}
            sx={{
                // width: '100%',
                borderBottom: '1px solid #f9f9f9d8',
                '.Mui-selected': {
                    fontWeight: 'bold',
                },
            }}>
            {children}
        </Tabs>
    );
};

export default CustomTabs;
