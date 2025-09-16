import CustomTabs from '@/shared/components/CustomTabs'
import { CustomTabPanel } from '@/shared/components/Tab/CustomTabPanel';
import { useTrainingApi } from 'modules/player/hooks/api/useTrainingApi';
import { useActiveTrainingApi } from 'modules/player/hooks/api/useActiveTrainingApi';
import { useTabManager } from '@/shared/hooks/useTabManager';
import TrainingReportsTab from './tabs/TrainingReportsTab';
import ScheduleTrainingTab from './tabs/ScheduleTrainingTab';
import { Box, Card, Tab } from '@mui/material';

const TrainingView = () => {

    const { selectedTab, handleTabChange } = useTabManager();
    const { data: trainings, isLoading } = useTrainingApi();
    const { data: playersWithActiveTrainings } = useActiveTrainingApi();

    return (
        <Card sx={{ padding: '8px' }} >
            <Box
                sx={{ display: 'flex', justifyContent: 'center', width: '100%' }}
                paddingY={1}
                position={'relative'}>
                <CustomTabs selectedTab={selectedTab} handleChange={handleTabChange}>
                    <Tab label="Trainings Report" />
                    <Tab label="Schedule Training" />
                </CustomTabs>
            </Box>
            <Box sx={{ width: '100%' }}>
                <CustomTabPanel value={selectedTab} index={0}>
                    <TrainingReportsTab trainings={trainings?.trainings} loading={isLoading} />
                </CustomTabPanel>
                <CustomTabPanel value={selectedTab} index={1}>
                    <ScheduleTrainingTab playersWithActiveTrainings={playersWithActiveTrainings} />
                </CustomTabPanel>
            </Box>
        </Card>
    )
}

export default TrainingView