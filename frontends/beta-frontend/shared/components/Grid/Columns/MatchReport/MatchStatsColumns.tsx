import { GridColDef } from '@mui/x-data-grid';
import { Box, Typography } from '@mui/material';
import ColHeader from '../common/components/ColHeader';
import { PITCH_AREA_SHORTCUTS } from '@/shared/models/match/PitchArea';
import { PlayerPositionAbbreviation } from '@/shared/models/player/PlayerPosition';
import { formatName } from '@/shared/utils/PlayerUtils';
import { getColumnConfig } from '../common/config/ColumnsConfig';
import CustomTooltip from 'modules/match/components/report/tooltips/CustomTooltip';

export const matchStatsColumns = () => {
  const columns: GridColDef[] = [
    {
      ...getColumnConfig("left"),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
      ...getColumnConfig(),
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
