import { theme } from '@/libs/material/theme'
import Grid from '@/shared/components/Grid/Grid'
import { CircularProgress, SelectChangeEvent, useMediaQuery } from '@mui/material'
import { PlayerScheduledTraningResponse } from 'modules/player/types/TrainingResponses'
import React, { useMemo, useState } from 'react'
import { ScheduleTrainingsColumns } from '../columns/ScheduleTrainingsColumns'
import { PlayerSkill } from '@/shared/models/player/PlayerSkill'
import { useTrainingApi } from 'modules/player/hooks/api/useTrainingApi'
import { PlayerPosition } from '@/shared/models/player/PlayerPosition'
import TrainingFilter from '../filter/TrainingFilter'
interface ScheduleTrainingTabProps {
    playersWithActiveTrainings: PlayerScheduledTraningResponse[]
}

const ScheduleTrainingTab: React.FC<ScheduleTrainingTabProps> = ({ playersWithActiveTrainings }) => {
    const [positionFilter, setPositionFilter] = useState<PlayerPosition>();
    const { handleScheduleTrainingRequest } = useTrainingApi();

    const handleSkillCellClick = (
        skillToTrain: PlayerSkill | undefined,
        playerIdToTrain: string,
    ) => {
        handleScheduleTrainingRequest({ skill: skillToTrain }, playerIdToTrain);
    };

    const isXs = useMediaQuery(theme.breakpoints.down('sm'));
    const memoizedColumnsScheduled = useMemo(
        () => ScheduleTrainingsColumns(handleSkillCellClick, isXs),
        [isXs],
    );

    const handlePositionChange = (event: SelectChangeEvent) => {
        setPositionFilter(event.target.value as PlayerPosition);
    };

    return (
        <>
            <TrainingFilter handlePositionChange={handlePositionChange} positionFilter={positionFilter} />
            {playersWithActiveTrainings ? (
                <Grid
                    sx={{
                        marginTop: '16px',
                        maxHeight: '500px',
                        minHeight: '400px',
                    }}
                    hideFooter
                    loading={false}
                    getRowId={(row) => `${row.player.id}`}
                    rows={playersWithActiveTrainings || []}
                    columns={memoizedColumnsScheduled}
                />
            ) : (
                <CircularProgress />
            )}
        </>
    )
}

export default ScheduleTrainingTab