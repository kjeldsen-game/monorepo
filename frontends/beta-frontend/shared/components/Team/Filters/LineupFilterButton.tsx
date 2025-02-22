import { Button } from '@mui/material';
import React from 'react';

interface LineupFilterButtonProps {
  name: string;
  handleClick: (filter: string) => void;
}

const LineupFilterButton: React.FC<LineupFilterButtonProps> = ({
  name,
  handleClick,
}) => {
  return (
    <Button
      onClick={() => {
        handleClick(name.toUpperCase());
      }}
      sx={{
        marginX: '4px',
        fontWeight: 'bold',
        background: '#FFF2CC',
        color: 'black',
        borderRadius: '10px',
        padding: '0px',
        '&:hover': {
          background: 'rgba(255, 242, 204, 0.5)',
        },
      }}>
      {name.toUpperCase()}
    </Button>
  );
};

export default LineupFilterButton;
