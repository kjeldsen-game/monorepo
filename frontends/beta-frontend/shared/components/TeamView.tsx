import {
  Alert,
  Box,
  Button,
  CircularProgress,
  Snackbar,
  SnackbarCloseReason,
  Theme,
  Tooltip,
  TooltipProps,
  darken,
  lighten,
  styled,
  tooltipClasses,
} from '@mui/material';
import TeamDetails from './TeamDetails';
import PlayerTactics from './PlayerTactics';
import TeamTactics from '@/shared/components/TeamTactics';
import { teamColumn } from '@/shared/components/Grid/Columns/TeamColumn';
import { SampleTeam } from '@/data/SampleTeam';
import Grid from './Grid/Grid';
import { Player } from '../models/Player';
import { Team } from '../models/Team';
import { useEffect, useMemo, useState } from 'react';
import checkTeamComposition from '../utils/TeamCompositionRules';
import TeamCompositionErrors from './TeamCompositionErrors';
import { CompositionError } from '../models/CompositionError';
import { PlayerLineupStatus } from '../models/PlayerLineupStatus';
import { light } from '@mui/material/styles/createPalette';
import { DataGrid } from '@mui/x-data-grid';

const CompositionTooltip = styled(({ className, ...props }: TooltipProps) => (
  <Tooltip {...props} classes={{ popper: className }} />
))(() => ({
  [`& .${tooltipClasses.tooltip}`]: {
    maxWidth: 500,
  },
}));

interface TeamProps {
  isEditing: boolean;
  team: Team | undefined;
  handlePlayerChange?: (value: Player) => void;
  onTeamUpdate?: () => void;
  alert: any;
  setAlert: any;
}

const TeamView: React.FC<TeamProps> = ({
  isEditing,
  team,
  handlePlayerChange,
  onTeamUpdate,
  alert,
  setAlert,
}: TeamProps) => {
  const [compositionErrors, setCompositionErrors] = useState<
    CompositionError[]
  >([]);

  const memoizedCheck = useMemo(
    () =>
      checkTeamComposition(
        team?.players.filter(
          (player) => player.status === PlayerLineupStatus.Active,
        ) ?? [],
      ),
    [team?.players],
  );

  const handleClose = (
    event: React.SyntheticEvent | Event,
    reason?: SnackbarCloseReason,
  ) => {
    if (reason === 'clickaway') {
      return;
    }

    setAlert((prev) => ({
      ...prev,
      open: false,
    }));
  };

  const memoizedColumns = useMemo(
    () => teamColumn(isEditing, handlePlayerChange),
    [isEditing, handlePlayerChange],
  );

  useEffect(() => {
    if (team?.players) {
      setCompositionErrors([...memoizedCheck]);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [team?.players]);

  const saveButton = () => {
    if (isEditing) {
      return (
        <Box
          sx={{
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'right',
          }}>
          <Snackbar
            onClose={handleClose}
            autoHideDuration={1500}
            open={alert.open}
            anchorOrigin={{ vertical: 'top', horizontal: 'center' }}>
            <Alert severity={alert.type} sx={{ width: '100%' }}>
              {alert.message}
            </Alert>
          </Snackbar>
          <CompositionTooltip
            disableHoverListener={compositionErrors.length === 0}
            placement={'left'}
            title={<TeamCompositionErrors errors={compositionErrors} />}>
            <span>
              <Button
                variant="contained"
                onClick={onTeamUpdate}
                // disabled={compositionErrors.length > 0}
              >
                Save
              </Button>
            </span>
          </CompositionTooltip>
        </Box>
      );
    }
  };

  const getBackgroundColor = (
    color: string,
    theme: Theme,
    coefficient: number,
  ) => ({
    backgroundColor: darken(color, coefficient),
    ...theme.applyStyles('light', {
      backgroundColor: lighten(color, coefficient),
    }),
  });

  const StyledDataGrid = styled(Grid)(({ theme }) => ({
    '& .super-app-theme--ACTIVE': {
      ...getBackgroundColor(theme.palette.success.main, theme, 0.7),
      '&:hover': {
        ...getBackgroundColor(theme.palette.info.main, theme, 0.6),
      },
      '&.Mui-selected': {
        ...getBackgroundColor(theme.palette.info.main, theme, 0.5),
        '&:hover': {
          ...getBackgroundColor(theme.palette.info.main, theme, 0.4),
        },
      },
    },
  }));

  return (
    <Box sx={{ width: '100%' }}>
      <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
        <TeamDetails name={team?.name} />
        <PlayerTactics />
        <TeamTactics />
      </Box>
      <Box sx={{ width: '100%' }}>
        {saveButton()}
        {team?.players ? (
          <StyledDataGrid
            rows={team?.players}
            columns={memoizedColumns}
            getRowClassName={(params) =>
              `super-app-theme--${params.row.status}`
            }
          />
        ) : (
          <CircularProgress />
        )}
      </Box>
    </Box>
  );
};
export default TeamView;
