import { useState } from 'react';

export const useTabManager = () => {
  const [selectedTab, setSelectedTab] = useState(0);

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  return {
    selectedTab,
    handleTabChange,
  };
};
