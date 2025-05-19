import { useTeamRepository } from '@/pages/api/team/useTeamRepository';
import { HealthAndSafety } from '@mui/icons-material';
import { Tooltip, Typography } from '@mui/material';
import Box from '@mui/material/Box';
import Grid from '../Grid/Grid';
import { useMemo } from 'react';
import { useTranslation } from 'react-i18next';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Player } from '@/shared/models/player/Player';
import { useSession } from 'next-auth/react';
import { positionComparator } from '@/shared/utils/GridUtils';
import CustomTooltip from './Tooltips/CustomTooltip';
import PressureDescriptionItem from '../Team/Modifiers/PressureDescriptionItem';
import { getModifierDescription } from '@/shared/utils/TeamModifiersUtils';
import { matchReportPlayercolumns } from '../Grid/Columns/MatchReportPlayerColumns';

interface MatchReportMetricsProps {
  sx?: React.CSSProperties;
  teamId: string;
  side: 'left' | 'right';
  players: Player[];
  teamReport: any;
  teamColor: string;
  stats: any;
}

export const MatchReportMetrics: React.FC<MatchReportMetricsProps> = ({
  sx,
  teamId,
  players,
  teamReport,
  teamColor,
  stats,
}) => {
  const { data: session } = useSession();

  const { data } = useTeamRepository(teamId, session?.accessToken);

  const { t } = useTranslation(['game']);

  const filterTeamPlayers = (players: Player[], data: any) => {
    if (!data) return [];

    return data
      .filter((player: Player) => players.some((p) => p.id === player.id))
      .map((player: Player) => {
        const matchedPlayer = players.find((p) => p.id === player.id);
        return matchedPlayer
          ? { ...player, position: matchedPlayer.position }
          : player;
      });
  };

  const memoizedColumns = useMemo(
    () =>
      matchReportPlayercolumns(true, false, false, true, stats?.playersStats),
    [teamId],
  );

  const tacticDescription = getModifierDescription(
    teamReport.modifiers?.tactic,
  );
  const horizontalPressureDescription = getModifierDescription(
    teamReport.modifiers?.horizontalPressure,
  );
  const verticalPressureDescription = getModifierDescription(
    teamReport.modifiers?.verticalPressure,
  );

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        ...sx,
        paddingX: '10px',
      }}>
      <Box
        sx={{
          marginBottom: '20px',
          borderStyle: 'solid',
          borderWidth: '0 14px 0 14px',
          borderColor: teamColor,
          width: '100%',
          height: '120px',
          display: 'flex',
          flexFlow: 'column wrap',
          justifyContent: 'space-around',
          textAlign: 'left',
        }}>
        <Typography
          sx={{
            fontSize: '22px',
            color: teamColor,
            overflow: 'clip',
            height: '20px',
            lineHeight: '20px',
          }}>
          {data?.name || ''}
        </Typography>
        {/* <Box paddingLeft={'20px'} textAlign={'center'}>
          <Image width={90} height={90} alt="team logo" src={Team} />
        </Box> */}
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety sx={{ color: '#E52323' }} />
          <CustomTooltip
            tooltipContent={
              <PressureDescriptionItem
                name={'Horizontal Pressure'}
                description={horizontalPressureDescription}
              />
            }>
            <Typography
              sx={{ color: '#E52323' }}
              fontSize="18px"
              fontWeight={700}>
              {convertSnakeCaseToTitleCase(
                teamReport.modifiers?.horizontalPressure,
              )}
            </Typography>
          </CustomTooltip>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety sx={{ color: '#E52323' }} />
          <CustomTooltip
            tooltipContent={
              <PressureDescriptionItem
                name={'Vertical Pressure'}
                description={verticalPressureDescription}
              />
            }>
            <Typography
              sx={{ color: '#E52323' }}
              fontSize="18px"
              fontWeight={700}>
              {convertSnakeCaseToTitleCase(
                teamReport.modifiers?.verticalPressure,
              )}
            </Typography>
          </CustomTooltip>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety sx={{ color: '#E52323' }} />
          <CustomTooltip
            tooltipContent={
              <PressureDescriptionItem
                name={'Tactic'}
                description={tacticDescription}
              />
            }>
            <Typography
              sx={{ color: '#E52323' }}
              fontSize="18px"
              fontWeight={700}>
              {convertSnakeCaseToTitleCase(teamReport.modifiers?.tactic)}
            </Typography>
          </CustomTooltip>
        </Box>
      </Box>
      <Box>
        <Grid
          sx={{
            '& .MuiDataGrid-columnSeparator': {
              display: 'none',
            },
            '& .MuiDataGrid-cell': {
              padding: 0,
            },
            '& .MuiDataGrid-columnHeaders': {
              padding: 0,
            },
            '& .MuiDataGrid-columnHeader': {
              padding: 0,
            },
          }}
          showColumnRightBorder
          showCellRightBorder
          disableColumnMenu={true}
          rowHeight={40}
          // disableExtendRowFullWidth
          rows={
            filterTeamPlayers(players, data?.players)?.sort(
              positionComparator,
            ) || []
          }
          columns={memoizedColumns}
        />
      </Box>
    </Box>
  );
};

export default MatchReportMetrics;
