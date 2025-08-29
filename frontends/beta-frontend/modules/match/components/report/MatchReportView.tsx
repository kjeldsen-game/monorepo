import { useMatch } from 'modules/match/hooks/useMatch'
import MatchReportTeamStats from './stats/MatchReportTeamStats';
import { useTeamApi } from 'modules/player/hooks/api/useTeamApi';
import { Box, Tab, useMediaQuery } from '@mui/material';
import { theme } from '@/libs/material/theme';
import { TeamRole } from '@/shared/models/MatchReport';
import CustomTabs from '@/shared/components/CustomTabs';
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel';
import MatchReportStatsV2 from './stats/MatchReportStatsV2';
import MatchEvents from './matchEvents/MatchEvents';
import { useState } from 'react';
import { useEventBuilder } from 'modules/match/hooks/logic/useEventBuilder';
import PreAlphaAlert from '@/shared/components/PreAlphaAlert';

const MatchReportView = () => {

    const { data } = useMatch();
    const { data: homeTeam } = useTeamApi(data?.home.id);
    const { data: awayTeam } = useTeamApi(data?.away.id);
    const isXs = useMediaQuery(theme.breakpoints.down('sm'));

    const [selectedTab, setSelectedTab] = useState<number>(0);
    const [selectedChildTab, setSelectedChildTab] = useState<number>(0);

    const handleChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedTab(newValue);
    };

    const handleChildChange = (event: React.SyntheticEvent, newValue: number) => {
        setSelectedChildTab(newValue);
    };

    const { possesions, highlights } = useEventBuilder(data?.matchReport?.plays)

    return (
        <Box display={'flex'}>
            {!isXs &&
                <MatchReportTeamStats teamRole={TeamRole.HOME} players={homeTeam?.players} teamModifiers={data?.home?.modifiers} />
            }
            <Box width={!isXs ? '50%' : '100%'} marginX={2}  >
                <Box sx={{ background: 'white' }} height={'auto'}
                    padding={1}
                    borderRadius={1}
                    width={'100%'}
                    boxShadow={1}
                    display={'flex'} flexDirection={'column'} alignItems={'center'}>
                    <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
                        <Tab label="Match Stats" />
                        <Tab label="Match Report" />
                        <Tab label="Highlights" />
                    </CustomTabs>

                    {isXs && selectedTab === 0 &&
                        <CustomTabs selectedTab={selectedChildTab} handleChange={handleChildChange}>
                            <Tab label="Match" />
                            <Tab label="Home Team" />
                            <Tab label="Away Team" />
                        </CustomTabs>
                    }

                    <CustomTabPanel sx={{ width: '100%' }} value={isXs && selectedTab === 0 ? selectedChildTab : selectedTab} index={0}>
                        <MatchReportStatsV2 homeStats={data?.matchReport?.homeStats} awayStats={data?.matchReport?.awayStats} />
                    </CustomTabPanel>


                    <CustomTabPanel sx={{ width: '100%' }} value={selectedTab} index={1}>
                        <MatchEvents possessions={possesions} />
                    </CustomTabPanel>
                    <CustomTabPanel sx={{ width: '100%' }} value={selectedTab} index={2}>
                        <MatchEvents possessions={highlights} />
                    </CustomTabPanel>

                    {isXs && selectedTab === 0 && <>
                        <CustomTabPanel sx={{ width: '100%' }} value={selectedChildTab} index={1}>
                            <MatchReportTeamStats teamRole={TeamRole.HOME} players={homeTeam?.players} teamModifiers={data?.home?.modifiers} />
                        </CustomTabPanel>
                        <CustomTabPanel sx={{ width: '100%' }} value={selectedChildTab} index={2}>
                            <MatchReportTeamStats teamRole={TeamRole.AWAY} players={awayTeam?.players} teamModifiers={data?.away?.modifiers} />
                        </CustomTabPanel>
                    </>}
                </Box >
            </Box >

            {!isXs &&
                <MatchReportTeamStats teamRole={TeamRole.AWAY} players={awayTeam?.players} teamModifiers={data?.away?.modifiers} />
            }
        </Box>
    )
}

export default MatchReportView