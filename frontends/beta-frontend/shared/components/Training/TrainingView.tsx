import { Box, CircularProgress, Tab } from '@mui/material';
import { useMemo, useState } from 'react';
import Grid from '../Grid/Grid';
import DashboardLink from '../DashboardLink';
import { trainingColumn } from '../Grid/Columns/TrainingColumns';
import Collapsible from '../Collapsible';
import CustomTabs from '../CustomTabs';
import { CustomTabPanel } from '../Tab/CustomTabPanel';
import { trainingScheduleColumns } from '../Grid/Columns/TrainingScheduleColumns';
import TrainingModal from './TrainingModal';
import { PlayerSkill } from '@/shared/models/PlayerSkill';

interface TrainingViewProps {
  trainings: any;
  isLoading: boolean;
  players: any;
}

const TrainingView: React.FC<TrainingViewProps> = ({
  trainings,
  isLoading,
  players,
}: TrainingViewProps) => {
  const handleSkillCellClick = (
    skillToTrain: PlayerSkill | undefined,
    skillUnderTraining: PlayerSkill | undefined,
    playerIdToTrain: string,
  ) => {
    setSkillUnderTraining(skillUnderTraining);
    setSkillToTraing(skillToTrain);
    setOpen(true);
    setPlayerToTrain(playerIdToTrain);
  };

  const memoizedColumnsReport = useMemo(() => trainingColumn(), []);
  const memoizedColumnsScheduled = useMemo(
    () => trainingScheduleColumns(handleSkillCellClick),
    [],
  );

  const [selectedTab, setSelectedTab] = useState(0);
  const [open, setOpen] = useState<boolean>(false);
  const [skillToTrain, setSkillToTraing] = useState<PlayerSkill | undefined>(
    undefined,
  );
  const [skillUnderTraining, setSkillUnderTraining] = useState<
    PlayerSkill | undefined
  >(undefined);
  const [playerToTraing, setPlayerToTrain] = useState<string>('');
  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setSelectedTab(newValue);
  };

  const handleCloseModal = () => {
    setOpen(false);
    setPlayerToTrain('');
  };

  return (
    <Box sx={{ width: '100%' }}>
      <DashboardLink children={'Back to Dashboard'} />

      <Box>
        <CustomTabs selectedTab={selectedTab} handleChange={handleChange}>
          <Tab label="Trainings Report" />
          <Tab label="Schedule Training" />
        </CustomTabs>
      </Box>

      <Box sx={{ width: '100%' }}>
        <TrainingModal
          open={open}
          handleClose={handleCloseModal}
          skillToTrain={skillToTrain}
          skillUnderTraining={skillUnderTraining}
          playerToTrain={playerToTraing}
        />
        <CustomTabPanel value={selectedTab} index={0}>
          {trainings?.trainings ? (
            Object.entries(trainings?.trainings).map(
              ([date, trainingList]: [string, any]) => (
                <Collapsible
                  open={false}
                  key={`${date}'s Training Report`}
                  title={`Training Report from ${date}`}>
                  <Grid
                    sx={{ marginTop: '16px' }}
                    loading={isLoading}
                    getRowId={(row) =>
                      `${row.player.name} + ${row.skill} + ${row.actualPoints}`
                    }
                    rows={trainingList}
                    columns={memoizedColumnsReport}
                  />
                </Collapsible>
              ),
            )
          ) : (
            <CircularProgress />
          )}
        </CustomTabPanel>
        <CustomTabPanel value={selectedTab} index={1}>
          {players ? (
            <Grid
              sx={{ marginTop: '16px' }}
              loading={isLoading}
              getRowId={(row) => `${row.player.id}`}
              rows={players}
              columns={memoizedColumnsScheduled}
            />
          ) : (
            <CircularProgress />
          )}
        </CustomTabPanel>
      </Box>
    </Box>
  );
};

export default TrainingView;
