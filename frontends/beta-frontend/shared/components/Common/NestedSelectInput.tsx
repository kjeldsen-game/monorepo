import { useState } from 'react';
import { Menu, MenuItem, Button } from '@mui/material';
import { ChevronRight } from 'lucide-react';

const NestedSelectInput = () => {
  const [anchorEl, setAnchorEl] = useState(null);
  const [subAnchorEl, setSubAnchorEl] = useState(null);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
    setSubAnchorEl(null);
  };

  const handleSubmenuOpen = (event) => {
    setSubAnchorEl(event.currentTarget);
  };

  return (
    <div>
      <Button variant="contained" onClick={handleClick}>
        Menu
      </Button>
      <Menu anchorEl={anchorEl} open={Boolean(anchorEl)} onClose={handleClose}>
        <MenuItem onClick={handleClose}>Item 1</MenuItem>
        <MenuItem
          onMouseEnter={handleSubmenuOpen}
          onMouseLeave={() => setSubAnchorEl(null)}>
          Submenu <ChevronRight className="w-4 h-4" />
        </MenuItem>
        <Menu
          anchorEl={subAnchorEl}
          open={Boolean(subAnchorEl)}
          onClose={() => setSubAnchorEl(null)}>
          <MenuItem onClick={handleClose}>Sub Item 1</MenuItem>
          <MenuItem onClick={handleClose}>Sub Item 2</MenuItem>
        </Menu>
        <MenuItem onClick={handleClose}>Item 3</MenuItem>
      </Menu>
    </div>
  );
};

export default NestedDropdown;
