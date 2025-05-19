import { Box, Typography } from '@mui/material';
import React, { useState } from 'react';
import MatchStatsModal from './MatchStatsModal';
import MarketButton from '../Market/MarketButton';
import { MatchReport } from '@/shared/models/match/MatchReport';
import { Player } from '@/shared/models/player/Player';

interface MatchStatsProps {
  report: MatchReport;
}

const MatchStats: React.FC<MatchStatsProps> = ({ report }) => {
  const [open, setOpen] = useState<boolean>(false);

  const handleCloseModal = () => {
    setOpen(false);
  };

  // console.log(report.home.players);
  // console.log(report.away.players);

  const homePlayersWRole = report.home.players.map((player: Player) => {
    return player.name + ' [' + player.teamRole?.substring(0, 1) + ']';
  });

  const awayPlayersWRole = report.away.players.map((player) => {
    return player.name + ' [' + player.teamRole?.substring(0, 1) + ']';
  });

  const playersWRole = homePlayersWRole.concat(awayPlayersWRole);

  return (
    <Box
      display={'flex'}
      flexDirection={'column'}
      alignItems={'center'}
      width={'400px'}
      sx={{ background: '#F8F8F8', padding: '20px' }}>
      <MatchStatsModal
        players={playersWRole}
        data={report.plays}
        open={open}
        handleCloseModal={handleCloseModal}
      />
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.goals}</Typography>
        <Typography>Score</Typography>
        <Typography>{report.awayStats.goals}</Typography>
      </Box>
      {/* <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Possesion %</Typography>
        <Typography>-</Typography>
      </Box> */}
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.shots}</Typography>
        <Typography>Total Shots</Typography>
        <Typography>{report.awayStats.shots}</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.missed}</Typography>{' '}
        <Typography>Missed Shots</Typography>
        <Typography>{report.awayStats.missed}</Typography>{' '}
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.saved}</Typography>{' '}
        <Typography>Saves</Typography>
        <Typography>{report.awayStats.saved}</Typography>{' '}
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.passes}</Typography>
        <Typography>Passes</Typography>
        <Typography>{report.awayStats.passes}</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.missedPasses}</Typography>
        <Typography>Missed Passes</Typography>
        <Typography>{report.awayStats.missedPasses}</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.homeStats.tackles}</Typography>
        <Typography>Tackles</Typography>
        <Typography>{report.awayStats.tackles}</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Yellow cards</Typography>
        <Typography>-</Typography>
      </Box>
      <MarketButton sx={{ marginTop: '20px' }} onClick={() => setOpen(true)}>
        Show Stats Table
      </MarketButton>
    </Box>
  );
};

export default MatchStats;
