import React from 'react'
import SpeedDial from '@mui/material/SpeedDial';
import SpeedDialIcon from '@mui/material/SpeedDialIcon';
import SpeedDialAction from '@mui/material/SpeedDialAction';
import { theme } from '@/libs/material/theme';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import GppMaybeIcon from '@mui/icons-material/GppMaybe';
import EditIcon from '@mui/icons-material/Edit';
import CloseIcon from '@mui/icons-material/Close';
import AutorenewIcon from '@mui/icons-material/Autorenew';
import { useMediaQuery } from '@mui/material';


interface LineupSpeedDialProps {
    isEdit?: boolean;
    isFormationValid?: boolean;
    isEditOnClick?: () => void;
    openModalValidationOnClick?: () => void;
    toogleIsXsPlayers?: (value: boolean) => void;
}

const LineupSpeedDial: React.FC<LineupSpeedDialProps> = ({ isEdit, isFormationValid, isEditOnClick, openModalValidationOnClick, toogleIsXsPlayers }) => {

    const isXs = useMediaQuery(theme.breakpoints.down("sm"));

    const actions = [
        ...isXs ? [{ icon: <AutorenewIcon />, name: 'Switch lineup', onClick: toogleIsXsPlayers }] : [],
        { icon: isEdit ? <CloseIcon /> : <EditIcon />, name: 'Edit Lineup', onClick: isEditOnClick },
        { icon: isFormationValid ? <VerifiedUserIcon /> : <GppMaybeIcon />, name: 'Lineup Validation', onClick: openModalValidationOnClick },
    ];

    return (
        <SpeedDial
            sx={{
                '& .MuiSpeedDial-fab': {
                    borderRadius: 1,
                    width: 30,
                    height: 30,
                    minWidth: 20,
                    minHeight: 20,
                    background: theme.palette.secondary.main,
                    '&:hover': {
                        border: 'none',
                        backgroundColor: theme.palette.secondary.main
                    },
                },
                position: 'absolute',
                bottom: '-15px',
                right: 0,
                transform: 'translate(25%, 0%)',
            }}
            ariaLabel="SpeedDial basic example"
            icon={<SpeedDialIcon sx={{ background: theme.palette.secondary.main, color: theme.palette.common.white }} />}
        >
            {actions.map((action) => (
                <SpeedDialAction
                    sx={{ background: theme.palette.primary.main, color: theme.palette.secondary.main }}
                    key={action.name}
                    icon={action.icon}
                    slotProps={{
                        tooltip: {
                            title: action.name,
                        },
                    }}
                    onClick={action.onClick}
                />
            ))}
        </SpeedDial>
    )
}

export default LineupSpeedDial