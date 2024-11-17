import Box from '@mui/material/Box';
import { CircularProgress, Typography } from '@mui/material';
import Avatar from '@/shared/components/Avatar';
import TransferWithinAStationIcon from '@mui/icons-material/TransferWithinAStation';
import ElderlyIcon from '@mui/icons-material/Elderly';
import { FC } from 'react';
import { PlayerStats } from '@/data/SamplePlayer';
import { CommitSharp } from '@mui/icons-material';

const playerStatsStyle = {
  background: 'linear-gradient(90deg, #29B6F6 0%, #1C97CE 100%)',
  borderRadius: '4px',
  color: 'white',
  width: '170px',
  height: '28px',
  margin: '2px 0',
  paddingLeft: '1rem',
};

const playerStatsStyle2 = {
  background: 'linear-gradient(90deg, #F68B29 0%, #C56D1C 100%)',
  borderRadius: '4px',
  color: 'white',
  width: '170px',
  height: '28px',
  margin: '2px 0',
  paddingLeft: '1rem',
};

const playerStatsStyle3 = {
  background: 'linear-gradient(90deg, #62D160 0%, #429F40 100%)',
  borderRadius: '4px',
  color: 'white',
  width: '170px',
  height: '28px',
  margin: '2px 0',
  paddingLeft: '1rem',
};

type PlayerDetailsProps = {
  player: PlayerStats;
};

export const PlayerDetails: FC<PlayerDetailsProps> = ({ player }) => {
  if (player === undefined) return <CircularProgress></CircularProgress>;

  return (
    <Box
      sx={{
        display: 'flex',
        marginBottom: '2rem',
        alignItems: 'center',
      }}>
      <Avatar />
      <Box
        sx={{
          marginLeft: '3rem',
        }}>
        <Typography variant="body1">
          <strong>{player.name}</strong>
        </Typography>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <TransferWithinAStationIcon
            sx={{
              fontSize: 'large',
              color: '#A4BC10',
              marginRight: '4px',
            }}
          />
          <Typography variant="body1" sx={{ color: '#A4BC10' }}>
            {player.position}
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <ElderlyIcon
            sx={{
              fontSize: 'large',
              color: 'black',
              marginRight: '4px',
            }}
          />
          <Typography variant="body1">{player.age?.years}</Typography>
        </Box>
      </Box>
      {player.actualSkills !== undefined ? (
        <>
          <Box sx={{ marginLeft: '2rem' }}>
            <Typography variant="body1" component="p" sx={playerStatsStyle3}>
              {player.actualSkills.DEFENSIVE_POSITIONING?.PlayerSkills.actual} -
              Defense
            </Typography>
            <Typography variant="body1" component="p" sx={playerStatsStyle2}>
              {player.actualSkills.BALL_CONTROL?.PlayerSkills.actual} -
              BallControl
            </Typography>
            <Typography variant="body1" component="p" sx={playerStatsStyle2}>
              {player.actualSkills.SCORING?.PlayerSkills.actual} - Score
            </Typography>
          </Box>
          <Box
            sx={{
              marginLeft: '2rem',
            }}>
            <Typography variant="body1" component="p" sx={playerStatsStyle3}>
              {player.actualSkills.TACKLING?.PlayerSkills.actual} - Tackling
            </Typography>
            <Typography variant="body1" component="p" sx={playerStatsStyle}>
              {player.actualSkills.CONSTITUTION.PlayerSkills.actual} -
              Constitution
            </Typography>
            <Typography variant="body1" component="p" sx={playerStatsStyle2}>
              {player.actualSkills.PASSING?.PlayerSkills.actual} - Passing
            </Typography>
          </Box>
        </>
      ) : (
        <></>
      )}
    </Box>
  );
};

export default PlayerDetails;
