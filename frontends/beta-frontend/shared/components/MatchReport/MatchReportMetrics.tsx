import { useTeamRepository } from '@/pages/api/team/useTeamRepository';
import { HealthAndSafety } from '@mui/icons-material';
import { Typography } from '@mui/material';
import Box from '@mui/material/Box';
import Grid from '../Grid/Grid';
import { useMemo } from 'react';
import { simpleTeamColumn } from '../Grid/Columns/SimpleTeamColumn';
import { useTranslation } from 'react-i18next';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Player } from '@/shared/models/Player';
import { TABLE_PLAYER_POSITION_ORDER } from '@/shared/models/PlayerPosition';
import { useSession } from 'next-auth/react';

interface MatchReportMetricsProps {
  sx?: React.CSSProperties;
  teamId: string;
  side: 'left' | 'right';
  players: any;
  teamReport: any;
  teamColor: string;
}

export const MatchReportMetrics: React.FC<MatchReportMetricsProps> = ({
  sx,
  teamId,
  players,
  teamReport,
  teamColor,
}) => {
  const { data: session } = useSession();
  const { data } = useTeamRepository(teamId, session?.accessToken);

  const { t } = useTranslation(['game']);

  const filterTeamPlayers = (players: Player[], data: any) => {
    if (data != undefined) {
      return data.filter((player: Player) =>
        players.some((p) => p.id === player.id),
      );
    }
  };

  function positionComparator(a: Player, b: Player): number {
    const indexA = TABLE_PLAYER_POSITION_ORDER.indexOf(a.position);
    const indexB = TABLE_PLAYER_POSITION_ORDER.indexOf(b.position);

    return (
      (indexA === -1 ? Number.MAX_SAFE_INTEGER : indexA) -
      (indexB === -1 ? Number.MAX_SAFE_INTEGER : indexB)
    );
  }

  const memoizedColumns = useMemo(() => simpleTeamColumn(t), [teamId]);

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
            color: '#A4BC10',
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
          <Typography
            sx={{ color: '#E52323' }}
            fontSize="18px"
            fontWeight={700}>
            {convertSnakeCaseToTitleCase(
              teamReport.modifiers?.horizontalPressure,
            )}
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety sx={{ color: '#E52323' }} />
          <Typography
            sx={{ color: '#E52323' }}
            fontSize="18px"
            fontWeight={700}>
            {convertSnakeCaseToTitleCase(
              teamReport.modifiers?.verticalPressure,
            )}
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <HealthAndSafety sx={{ color: '#E52323' }} />
          <Typography
            sx={{ color: '#E52323' }}
            fontSize="18px"
            fontWeight={700}>
            {convertSnakeCaseToTitleCase(teamReport.modifiers?.tactic)}
          </Typography>
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
              padding: 0, // Removes padding from the column header container
            },
            '& .MuiDataGrid-columnHeader': {
              padding: 0, // Removes padding from each individual header cell
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
