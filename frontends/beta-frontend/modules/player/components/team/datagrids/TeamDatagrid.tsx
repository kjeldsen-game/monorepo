import React, { useMemo } from 'react';
import { Player } from '@/shared/models/player/Player';
import {
    TABLE_PLAYER_POSITION_ORDER_DEFENDERS,
    TABLE_PLAYER_POSITION_ORDER_FORWARDS,
    TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS,
    TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS,
} from '@/shared/models/player/PlayerPosition';
import { LineupColumns } from '../columns/LineupColumns';
import Grid from '@/shared/components/Grid/Grid';
import { SxProps, useMediaQuery } from '@mui/material';
import { theme } from '@/libs/material/theme';

interface TeamDataGridProps {
    rows: Player[];
    onSelectChange: (...args: any[]) => void;
    onButtonClick?: (player: any) => void;
    activePlayer?: Player;
    isEditing?: boolean;
    isXsPlayers?: boolean;
    sx?: SxProps;
}

const TeamDataGrid: React.FC<TeamDataGridProps> = ({
    rows,
    sx,
    onButtonClick,
    onSelectChange,
    activePlayer,
    isEditing,
    isXsPlayers
}) => {

    const isXs = useMediaQuery(theme.breakpoints.down("sm"));

    const columns = useMemo(
        () => LineupColumns(isEditing, onButtonClick, activePlayer, onSelectChange, isXs, isXsPlayers),
        [onButtonClick, activePlayer, isEditing, onSelectChange, isXs, isXsPlayers],
    );

    return (
        <Grid
            hideFooter
            sx={{
                // maxHeight: '600px',
                minHeight: '200px',
                '& .super-app-theme--goalkeepers': {
                    boxShadow: 'inset 3px 0 0 0 #fff2cc',
                },
                '& .super-app-theme--defenders': {
                    boxShadow: 'inset 3px 0 0 0 #ABCAA9',
                },
                '& .super-app-theme--midfielders': {
                    boxShadow: 'inset 3px 0 0 0 #E99898',
                },
                '& .super-app-theme--forwards': {
                    boxShadow: 'inset 3px 0 0 0 #CCDCFC',
                },
                '& .super-app-theme--selected': {
                    backgroundColor: 'rgba(169, 169, 169, 0.4) !important',
                },
                '& .super-app-theme--notselected': {
                    opacity: '10% !important',
                },
                ...sx,
            }}
            rows={rows || []}
            columns={columns}
            initialState={{
                sorting: { sortModel: [{ field: 'playerPosition', sort: 'asc' }] },
            }}
            getRowClassName={(params) => {
                const position = params.row.position;
                const classes: string[] = [];

                if (TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS.includes(position)) {
                    classes.push('super-app-theme--goalkeepers');
                }
                if (TABLE_PLAYER_POSITION_ORDER_DEFENDERS.includes(position)) {
                    classes.push('super-app-theme--defenders');
                }
                if (TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS.includes(position)) {
                    classes.push('super-app-theme--midfielders');
                }
                if (TABLE_PLAYER_POSITION_ORDER_FORWARDS.includes(position)) {
                    classes.push('super-app-theme--forwards');
                }

                if (params.row.id === activePlayer?.id) {
                    classes.push('super-app-theme--selected');
                }
                if (activePlayer && params.row.id != activePlayer?.id) {
                    classes.push('super-app-theme--notselected');
                }

                return classes.join(' ');
            }}
        />
    );
};

export default TeamDataGrid;
