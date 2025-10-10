import CustomTabs from '@/shared/components/Tabs/CustomTabs'
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import { useTrainingApi } from 'modules/player/hooks/api/useTrainingApi';
import { useActiveTrainingApi } from 'modules/player/hooks/api/useActiveTrainingApi';
import { useTabManager } from '@/shared/hooks/useTabManager';
import TrainingReportsTab from './tabs/TrainingReportsTab';
import ScheduleTrainingTab from './tabs/ScheduleTrainingTab';
import { Box, Card, Tab } from '@mui/material';
import { useEffect, useState } from 'react';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { TrainingFilterProvider } from 'modules/player/contexts/TrainingFilterContext';
import { useSWRConfig } from 'swr';
import { TEAMS_API } from 'modules/player/services/teamApi';
import { useSession } from 'next-auth/react';

const TrainingView = () => {
    const { data: userData } = useSession();
    const { mutate } = useSWRConfig();
    const { selectedTab, handleTabChange } = useTabManager();
    const [positionFilter, setPositionFilter] = useState<PlayerPosition>();
    const { data: trainings, isLoading } = useTrainingApi();
    const { data: playersWithActiveTrainings, isLoading: isLoadingActive } = useActiveTrainingApi(positionFilter);

    const handlePositionChange = (event) => {
        console.log(positionFilter)
        setPositionFilter(event.target.value);
    };

    useEffect(() => {
        mutate(TEAMS_API + userData?.user?.teamId + 'active');
    }, [positionFilter])

    return (
        <TrainingFilterProvider position={positionFilter} handlePositionChange={handlePositionChange}>
            <Card sx={{ padding: '8px' }} >
                <CustomTabs sx={{ paddingBottom: 1 }} tabs={["Training Reports", "Schedule Training"]}
                    selectedTab={selectedTab} handleChange={handleTabChange} />
                <Box sx={{ width: '100%' }}>
                    <CustomTabPanel value={selectedTab} index={0}>
                        <TrainingReportsTab trainings={trainings?.trainings} loading={isLoading} />
                    </CustomTabPanel>
                    <CustomTabPanel value={selectedTab} index={1}>
                        <ScheduleTrainingTab loading={isLoadingActive} playersWithActiveTrainings={playersWithActiveTrainings} />
                    </CustomTabPanel>
                </Box>
            </Card>
        </TrainingFilterProvider>
    )
}

export default TrainingView