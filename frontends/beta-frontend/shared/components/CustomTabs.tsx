import { Box, Tabs } from '@mui/material';
import React, { useState } from 'react';

interface CustomTabsProps {
    children: React.ReactNode;
    selectedTab: any;
    handleChange: any;
}

const CustomTabs: React.FC<CustomTabsProps> = ({
    children,
    selectedTab,
    handleChange,
}: CustomTabsProps) => {
    return (
        <Tabs
            textColor="secondary"
            indicatorColor="secondary"
            value={selectedTab}
            onChange={handleChange}
            aria-label="basic tabs example"
            TabIndicatorProps={{
                style: {
                    backgroundColor: '#FF3F84',
                },
            }}
            sx={{
                '.Mui-selected': {
                    fontWeight: 'bold',
                },
            }}>
            {children}
        </Tabs>
    );
};

export default CustomTabs;
