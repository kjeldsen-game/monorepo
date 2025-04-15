import { Box, Typography } from '@mui/material';
import React, { useState } from 'react';
import MatchReportToCSVParser from './MatchReportToCSVParser';
import MatchStatsModal from './MatchStatsModal';
import MarketButton from '../Market/MarketButton';

interface MatchStatsProps {
  report: any;
}

const MatchStats: React.FC<MatchStatsProps> = ({ report }) => {
  const [open, setOpen] = useState<boolean>(false);

  const handleCloseModal = () => {
    setOpen(false);
  };

  console.log(report.home.players);
  console.log(report.away.players);

  const homePlayersWRole = report.home.players.map((player) => {
    return player.name + ' [' + player.teamRole.substring(0, 1) + ']';
  });

  const awayPlayersWRole = report.away.players.map((player) => {
    return player.name + ' [' + player.teamRole.substring(0, 1) + ']';
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
        data={report.matchReport.plays}
        open={open}
        handleCloseModal={handleCloseModal}
      />
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>{report.matchReport.homeScore}</Typography>
        <Typography>Score</Typography>
        <Typography>{report.matchReport.awayScore}</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Possesion %</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Shots</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Shots on target</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Passes</Typography>
        <Typography>-</Typography>
      </Box>
      <Box
        display={'flex'}
        sx={{ width: '80%' }}
        justifyContent={'space-between'}>
        <Typography>-</Typography>
        <Typography>Tackles</Typography>
        <Typography>-</Typography>
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
      {/* <MatchReportToCSVParser dataPass={report.matchReport.plays} /> */}
      {/* <MatchReportToCSVParser data={{ report.matchReport.plays }}></MatchReportToCSVParser> */}
    </Box>
  );
};

export default MatchStats;
