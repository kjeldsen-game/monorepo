import React, { useMemo } from 'react';
import Grid from '../Grid/Grid';
import { Player } from '@/shared/models/Player';
import { lineupColumn } from '../Grid/Columns/LineupColumn';
import { GridRowParams } from '@mui/x-data-grid';
import {
  TABLE_PLAYER_POSITION_ORDER_DEFENDERS,
  TABLE_PLAYER_POSITION_ORDER_FORWARDS,
  TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS,
  TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS,
} from '@/shared/models/PlayerPosition';

interface TeamGridProps {
  rows: any;
  onSelectChange: (...args: any[]) => void;
  onButtonClick: (player: any) => void;
  sx?: object;
  activePlayer?: Player;
  isEditing: boolean;
}

const TeamGrid: React.FC<TeamGridProps> = ({
  rows,
  sx,
  onButtonClick,
  onSelectChange,
  activePlayer,
  isEditing,
}) => {
  const columns = useMemo(
    () => lineupColumn(isEditing, onButtonClick, onSelectChange),
    [onButtonClick, activePlayer, isEditing, onSelectChange],
  );

  return (
    <Grid
      autoHeight={false}
      sx={{
        maxHeight: '600px',
        minHeight: '400px',
        '& .super-app-theme--goalkeepers': {
          borderLeft: '3px solid #fff2cc', // Yellow border with 30% opacity
        },
        '& .super-app-theme--defenders': {
          borderLeft: '3px solid #ABCAA9', // Green border with 30% opacity
        },
        '& .super-app-theme--midfielders': {
          borderLeft: '3px solid #E99898', // Red border with 30% opacity
        },
        '& .super-app-theme--forwards': {
          borderLeft: '3px solid #CCDCFC', // Blue border with 30% opacity
        },
        '& .super-app-theme--selected': {
          backgroundColor: 'rgba(169, 169, 169, 0.4) !important',
        },
        '& .super-app-theme--notselected': {
          opacity: '10%',
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

export default TeamGrid;
