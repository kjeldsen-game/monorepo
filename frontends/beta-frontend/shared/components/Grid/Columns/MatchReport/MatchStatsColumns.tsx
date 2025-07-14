import { GridColDef } from '@mui/x-data-grid';
import { Box, Typography } from '@mui/material';
import ColHeader from '../common/components/ColHeader';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from '../common/config/ColumnsConfig';
import { PITCH_AREA_SHORTCUTS } from '@/shared/models/match/PitchArea';
import { PlayerPositionAbbreviation } from '@/shared/models/player/PlayerPosition';
import { formatName } from '@/shared/utils/PlayerUtils';
import CustomTooltip from '@/shared/components/MatchReport/Tooltips/CustomTooltip';

export const matchStatsColumns = () => {
  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'clock',
      renderHeader: () => <ColHeader header={'Clk'} align={'left'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip tooltipContent={<Typography>Clock</Typography>}>
            <Box>{params.row.clock}</Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'action',
      renderHeader: () => <ColHeader header={'Act'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip tooltipContent={<Typography>Action</Typography>}>
            <Box>{params.row.action}</Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'ballHeight',
      renderHeader: () => <ColHeader header={'Ball'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip tooltipContent={<Typography>Ball Height</Typography>}>
            <Box>{params.row.ballState.height}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'fieldArea',
      renderHeader: () => <ColHeader header={'Fiel'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip tooltipContent={<Typography>Field Area</Typography>}>
            <Box>{PITCH_AREA_SHORTCUTS[params.row.duel.pitchArea]}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'initiator',
      renderHeader: () => <ColHeader header={'Att'} />,
      minWidth: 130,
      renderCell: (params) => {
        return (
          <CustomTooltip tooltipContent={<Typography>Initiator</Typography>}>
            <Box>
              {formatName(params.row.duel.initiator.name)} [
              {PlayerPositionAbbreviation[params.row.duel.initiator.position]}]
              [{params.row.duel.initiator.teamRole.substring(0, 1)}]
            </Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'skillPointInitiator',
      renderHeader: () => <ColHeader header={'Pts'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Initiator Skill Value</Typography>}>
            <Box>{params.row.duel.initiatorStats.skillPoints}</Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'randomInitiator',
      renderHeader: () => <ColHeader header={'Ran'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Initiator Random</Typography>}>
            <Box>{params.row.duel.initiatorStats.performance.random}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'randomPrevInitiator',
      renderHeader: () => <ColHeader header={'RanPrev'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={
              <Typography>Initiator Previous Total Impact</Typography>
            }>
            <Box>
              {params.row.duel.initiatorStats.performance.previousTotalImpact}
            </Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'randomTotalInitiator',
      renderHeader: () => <ColHeader header={'RanTot'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Initiator Random Total</Typography>}>
            <Box>{params.row.duel.initiatorStats.performance.total}</Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'carryOverInitiator',
      renderHeader: () => <ColHeader header={'CarryO'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Initiator Carryover</Typography>}>
            <Box>{params.row.duel.initiatorStats.carryover}</Box>{' '}
          </CustomTooltip>
        );
        return;
      },
    },
    {
      ...baseColumnConfig,
      field: 'assitanceInitiator',
      renderHeader: () => <ColHeader header={'Assist'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Initiator Assistance</Typography>}>
            <Box>{params.row.duel.initiatorStats.assistance}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'totalInitiator',
      renderHeader: () => <ColHeader header={'Tot'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Initiator Total</Typography>}>
            <Box>{params.row.duel.initiatorStats.total}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'passReceiver',
      renderHeader: () => <ColHeader header={'Pass'} />,
      minWidth: 100,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Pass Receiver Total</Typography>}>
            <Box>
              {params.row.duel.receiver &&
                formatName(params.row.duel.receiver?.name)}
            </Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'challenger',
      minWidth: 130,
      renderHeader: () => <ColHeader header={'Def'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip tooltipContent={<Typography>Challenger</Typography>}>
            <Box>
              {params.row.duel.challenger && (
                <>
                  {formatName(params.row.duel.challenger.name)}
                  {' [' +
                    PlayerPositionAbbreviation[
                    params.row.duel.challenger.position
                    ] +
                    ']' +
                    ' [' +
                    params.row.duel.challenger.teamRole.substring(0, 1) +
                    ']'}
                </>
              )}
            </Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'skillPointChallenger',
      renderHeader: () => <ColHeader header={'Pts'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Challenger Skill Value</Typography>}>
            <Box>{params.row.duel.challengerStats.skillPoints}</Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'randomChallenger',
      renderHeader: () => <ColHeader header={'Ran'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Challenger Random</Typography>}>
            <Box>{params.row.duel.challengerStats.performance.random}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'randomPrevChallenger',
      renderHeader: () => <ColHeader header={'RanPrev'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={
              <Typography>Challenger Previous Total Impact</Typography>
            }>
            <Box>
              {params.row.duel.challengerStats.performance.previousTotalImpact}
            </Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'randomTotalChallenger',
      renderHeader: () => <ColHeader header={'RanTot'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Challenger Random Total</Typography>}>
            <Box>{params.row.duel.challengerStats.performance.total}</Box>
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'carryOverChallenger',
      renderHeader: () => <ColHeader header={'CarryO'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Challenger Carryover</Typography>}>
            <Box>{params.row.duel.challengerStats.carryover}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...baseColumnConfig,
      field: 'assitanceChallenger',
      renderHeader: () => <ColHeader header={'Assist'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Challenger Assistance</Typography>}>
            <Box>{params.row.duel.challengerStats.assistance}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
    {
      ...rightColumnConfig,
      field: 'totalChallenger',
      renderHeader: () => <ColHeader header={'Tot'} align={'right'} />,
      renderCell: (params) => {
        return (
          <CustomTooltip
            tooltipContent={<Typography>Challenger Total</Typography>}>
            <Box>{params.row.duel.challengerStats.total}</Box>{' '}
          </CustomTooltip>
        );
      },
    },
  ];

  return columns;
};
