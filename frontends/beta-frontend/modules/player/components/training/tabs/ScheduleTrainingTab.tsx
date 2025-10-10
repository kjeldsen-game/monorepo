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
    loading: boolean;
}

const ScheduleTrainingTab: React.FC<ScheduleTrainingTabProps> = ({ playersWithActiveTrainings, loading }) => {
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
            <Grid
                sx={{
                    marginTop: 1,
                    maxHeight: '500px',
                    minHeight: '400px',
                }}
                hideFooter
                loading={loading}
                getRowId={(row) => `${row.player.id}`}
                rows={playersWithActiveTrainings || []}
                columns={memoizedColumnsScheduled}
            />
        </>
    )
}

export default ScheduleTrainingTab