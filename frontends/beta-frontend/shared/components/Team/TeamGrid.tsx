import React, { useMemo } from 'react';
import Grid from '../Grid/Grid';
import { Player } from '@/shared/models/Player';
import { lineupColumn } from '../Grid/Columns/LineupColumn';
import { GridRowParams } from '@mui/x-data-grid';
import {
  PlayerPositionColorNew,
  TABLE_PLAYER_POSITION_ORDER_DEFENDERS,
  TABLE_PLAYER_POSITION_ORDER_FORWARDS,
  TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS,
  TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS,
} from '@/shared/models/PlayerPosition';

interface TeamGridProps {
  rows: any;
  onRowClick?: (params: GridRowParams) => void;
  sx?: object;
}

const TeamGrid: React.FC<TeamGridProps> = ({ rows, onRowClick, sx }) => {
  const columns = useMemo(() => lineupColumn(), []);

  return (
    <Grid
      sx={{
        '& .super-app-theme--goalkeepers': {
          borderLeft: '3px solid #fff2cc', // Yellow border with 30% opacity
        },
        '& .super-app-theme--defenders': {
          borderLeft: '3px solid #ABCAA9', // Green border with 30% opacity
          backgroundColor: 'rgba(169, 169, 169, 0.1)',
        },
        '& .super-app-theme--midfielders': {
          borderLeft: '3px solid #E99898', // Red border with 30% opacity
        },
        '& .super-app-theme--forwards': {
          borderLeft: '3px solid #CCDCFC', // Blue border with 30% opacity
          backgroundColor: 'rgba(169, 169, 169, 0.1)',
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
        let category = '';

        switch (true) {
          case TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS.includes(position):
            category = 'goalkeepers';
            break;
          case TABLE_PLAYER_POSITION_ORDER_DEFENDERS.includes(position):
            category = 'defenders';
            break;
          case TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS.includes(position):
            category = 'midfielders';
            break;
          case TABLE_PLAYER_POSITION_ORDER_FORWARDS.includes(position):
            category = 'forwards';
            break;
          default:
            category = '';
        }

        return `super-app-theme--${category}`;
      }}
      onRowClick={onRowClick}
    />
  );
};

export default TeamGrid;
