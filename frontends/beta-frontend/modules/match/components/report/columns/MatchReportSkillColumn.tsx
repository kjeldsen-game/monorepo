import { GridCellParams, GridColDef } from '@mui/x-data-grid';

import { ActualSkill, Player } from '@/shared/models/player/Player';
import { Box, Typography } from '@mui/material';
import {
  PlayerSkill,
  PlayerSkillToShortcut,
} from '@/shared/models/player/PlayerSkill';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';

export const MatchReportSkillColumn = (
  getValue: (row: any) => Player,
  primarySkill: PlayerSkill,
  secondarySkill: PlayerSkill | undefined,
  isXs?: boolean,
): GridColDef => {
  return {
    ...getColumnConfig(),
    field: primarySkill,
    ...(isXs ? { width: 30, maxWidth: 30, minWidth: 30 } : {}),
    renderHeader: () => (
      <>
        <ColHeader header={PlayerSkillToShortcut[primarySkill]}>
          <sup style={{ color: '#FF3F84' }}>
            {PlayerSkillToShortcut[secondarySkill]}
          </sup>
        </ColHeader>
      </>
    ),
    renderCell: (params: GridCellParams) => {
      const player = getValue(params.row);
      const skill =  player.skills[primarySkill] ??
        player.skills[secondarySkill!] ??
        undefined;
      return (
        <Box
          padding={0}
          flexDirection="column"
          textAlign="center"
          sx={{
            justifyContent: 'center',
            height: '100%',
            display: 'flex',
            overflow: 'hidden',
            textOverflow: 'ellipsis',
            maxWidth: '100%',
          }}>
          <Typography>
            {skill ? skill : ''}
          </Typography>
          {/* {skill && <PlayerSkillText skills={skills} />} */}
        </Box>
      );
    },
  };
};
