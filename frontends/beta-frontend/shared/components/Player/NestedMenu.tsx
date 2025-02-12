import React, { useState } from 'react';
import { Menu, MenuItem, Button } from '@mui/material';
import { PITCH_AREAS_ENUM } from '@/shared/models/PitchArea';
import MarketButton from '../Market/MarketButton';

const optionsWithSubmenus: Record<string, string[]> = {
  'Option 1': ['A1', 'A2', 'A3'],
  'Option 2': ['B1', 'B2', 'B3'],
  'Option 3': Object.values(PITCH_AREAS_ENUM), // Using ENUM dynamically
};

export const NestedMenu: React.FC = () => {
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [subMenuAnchorEl, setSubMenuAnchorEl] = useState<null | HTMLElement>(
    null,
  );
  const [openSubMenu, setOpenSubMenu] = useState(false);
  const [pendingOption, setPendingOption] = useState<string | null>(null); // Option hovered but not selected yet
  const [selectedOption, setSelectedOption] = useState<string | null>(null);
  const [selectedSubOption, setSelectedSubOption] = useState<string | null>(
    null,
  );

  const handleClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuItemClick = (option: string) => {
    if (!optionsWithSubmenus[option]) {
      setSelectedOption(option);
      setSelectedSubOption(null); // Reset sub-option if main option is selected
      setAnchorEl(null); // Close the main menu
    }
  };

  const handleSubMenuOpen = (
    event: React.MouseEvent<HTMLElement>,
    option: string,
  ) => {
    setPendingOption(option); // Store hovered option, but do not set selected yet
    setSubMenuAnchorEl(event.currentTarget);
    setOpenSubMenu(true);
  };

  const handleSubMenuClose = () => {
    setSubMenuAnchorEl(null);
    setOpenSubMenu(false);
    setPendingOption(null); // Reset pending option on close
  };

  const handleSubMenuItemClick = (subOption: string) => {
    setSelectedOption(pendingOption); // Confirm the main option selection
    setSelectedSubOption(subOption);
    setOpenSubMenu(false); // Close the sub-menu after selection
    setAnchorEl(null); // Close the main menu
  };

  return (
    <div>
      <MarketButton onClick={handleClick}>Player Order</MarketButton>

      {/* Main Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={() => setAnchorEl(null)}>
        {Object.keys(optionsWithSubmenus).map((option) => (
          <MenuItem
            key={option}
            onClick={() => handleMenuItemClick(option)}
            onMouseEnter={(event) =>
              optionsWithSubmenus[option] && handleSubMenuOpen(event, option)
            }>
            {option}
          </MenuItem>
        ))}
      </Menu>

      {/* Sub Menu (Opens on Hover) */}
      <Menu
        anchorEl={subMenuAnchorEl}
        open={openSubMenu}
        onClose={handleSubMenuClose}
        MenuListProps={{ onMouseLeave: handleSubMenuClose }} // Close on mouse leave
      >
        {(optionsWithSubmenus[pendingOption!] || []).map((subOption) => (
          <MenuItem
            key={subOption}
            onClick={() => handleSubMenuItemClick(subOption)}>
            {subOption}
          </MenuItem>
        ))}
      </Menu>
    </div>
  );
};
