import Box from '@mui/material/Box';
import MatchReportItem from './MatchReportItem';
import { MatchReport, Play } from '@/shared/models/MatchReport';
import CustomTabs from '../CustomTabs';
import { Tab } from '@mui/material';
import { useEffect, useState } from 'react';
import { CustomTabPanel } from '../Tab/CustomTabPanel';
import MatchStats from './MatchStats';
import MatchPossesion from './MatchPossesion';

interface MatchReportContentProps {
  report: MatchReport;
  sx?: React.CSSProperties;
}

export const MatchReportContent: React.FC<MatchReportContentProps> = ({
  report,
  sx,
}) => {
  const [selectedTab, setSelectedTab] = useState<number>(0);
  const [possesions, setPossesions] = useState<Play[][]>([]);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  const formatClock = (clock: number): string => {
    const totalMinutes = Math.floor(clock / 4);
    const seconds = (clock % 4) * 15;

    const formattedMinutes = String(totalMinutes).padStart(2, '0');
    const formattedSeconds = String(seconds).padStart(2, '0');

    return `${formattedMinutes}:${formattedSeconds}`;
  };

  useEffect(() => {
    let possesions: Play[][] = [];

    report.matchReport.plays.forEach((event: Play) => {
      let lastGroup = possesions.at(-1);

      if (
        lastGroup &&
        lastGroup[0].duel.initiator.id === event.duel.initiator.id
      ) {
        lastGroup.push(event);
      } else {
        possesions.push([event]);
      }
    });
    console.log(possesions);
    setPossesions(possesions);
  }, [report]);

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'start',
        height: '100%',
        overflow: 'auto',
        gap: '15px',
        padding: '0 5px 0 5px',
        width: '100%',
        ...sx,
      }}>
      <Box>
        <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
          <Tab label="Match Stats" />
          <Tab label="Match Report" />
          <Tab label="Highlights" />
        </CustomTabs>
      </Box>

      <Box>
        <CustomTabPanel value={selectedTab} index={0}>
          <MatchStats report={report}></MatchStats>
        </CustomTabPanel>

        <CustomTabPanel value={selectedTab} index={1}>
          <Box
            display={'flex'}
            flexDirection={'column'}
            alignItems={'center'}
            width={'100%'}
            sx={{ background: '#F8F8F8', padding: '20px' }}>
            <Box
              sx={{
                background: '#3D3D3D',
                width: '200px',
                height: '20px',
                color: 'white',
                borderRadius: '5px',
                textAlign: 'center',
                fontSize: '12px',
              }}>
              MATCH STARTS
            </Box>
            {possesions.map((possesion, index) => (
              <MatchPossesion
                possesion={possesion}
                homeId={report.home.id}
                awayId={report.away.id}
              />
            ))}
          </Box>
        </CustomTabPanel>
        <CustomTabPanel value={selectedTab} index={2}>
          <Box
            display={'flex'}
            flexDirection={'column'}
            alignItems={'center'}
            width={'100%'}
            sx={{ background: '#F8F8F8', padding: '20px' }}>
            <Box
              sx={{
                background: '#3D3D3D',
                width: '200px',
                height: '20px',
                color: 'white',
                borderRadius: '5px',
                textAlign: 'center',
                fontSize: '12px',
              }}>
              MATCH STARTS
            </Box>

            {report.matchReport.plays
              .filter((play: Play) => play.action === 'SHOOT')
              .map((play: Play, index: number) => (
                <Box
                  sx={{
                    width: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                  }}>
                  <Box
                    alignSelf={'center'}
                    fontSize={'8px'}
                    textAlign={'center'}
                    sx={{ background: '#A3A3A3', width: '40px' }}
                    borderRadius={'5px'}>
                    {formatClock(play.clock)}
                  </Box>
                  <Box
                    paddingY={'10px'}
                    display={'flex'}
                    flexDirection={'column'}
                    alignItems={
                      play.duel.initiator.teamRole === 'HOME' ? 'start' : 'end'
                    }>
                    <Box textAlign={'justify'} sx={{ width: '80%' }}>
                      <MatchReportItem
                        index={0}
                        key={index}
                        event={play}
                        homeId={report.homeId}
                        awayId={report.awayId}
                        isLast={true}
                      />
                    </Box>
                  </Box>
                </Box>
              ))}
          </Box>
        </CustomTabPanel>
      </Box>
    </Box>
  );
};

export default MatchReportContent;
