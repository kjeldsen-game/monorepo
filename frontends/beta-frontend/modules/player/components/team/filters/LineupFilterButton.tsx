import { theme } from '@/libs/material/theme';
import { Button, useMediaQuery } from '@mui/material';
import React from 'react';

interface LineupFilterButtonProps {
    name: string;
    handleClick: (filter: string) => void;
    active: string;
}

const LineupFilterButton: React.FC<LineupFilterButtonProps> = ({
    name,
    handleClick,
    active,
}) => {

    const isXs = useMediaQuery(theme.breakpoints.down("sm"))

    return (
        <Button
            onClick={() => {
                handleClick(name.toUpperCase());
            }}
            sx={{
                marginRight: '4px',
                fontWeight: 'bold',
                background: name === active ? 'rgb(245, 203, 77)' : '#FFF2CC',
                color: 'black',
                borderRadius: '10px',
                paddingX: isXs ? 3 : 2,
                paddingY: 0,
                ...(isXs && {
                    width: 30,
                    minWidth: 30,
                }),
                '&:hover': {
                    background: 'rgba(255, 242, 204, 0.5)',
                },
            }}>
            {isXs ? name.substring(0, 3).toUpperCase() : name.toUpperCase()}
        </Button >
    );
};

export default LineupFilterButton;
