import { theme } from '@/libs/material/theme'
import Grid from '@/shared/components/Grid/Grid'
import { Box, CircularProgress, Typography, useMediaQuery } from '@mui/material'
import { PlayerScheduledTraningResponse } from 'modules/player/types/TrainingResponses'
import React, { useMemo, useState } from 'react'
import { ScheduleTrainingsColumns } from '../columns/ScheduleTrainingsColumns'
import { PlayerSkill } from '@/shared/models/player/PlayerSkill'

import TrainingDialog from '../dialog/TrainingDialog'

interface ScheduleTrainingTabProps {
    playersWithActiveTrainings: PlayerScheduledTraningResponse[]
}

const ScheduleTrainingTab: React.FC<ScheduleTrainingTabProps> = ({ playersWithActiveTrainings }) => {

    const [open, setOpen] = useState<boolean>(false);
    const [skillToTrain, setSkillToTraing] = useState<PlayerSkill | undefined>(
        undefined,
    );
    const [skillUnderTraining, setSkillUnderTraining] = useState<
        PlayerSkill | undefined
    >(undefined);
    const [playerToTraing, setPlayerToTrain] = useState<string>('');

    const handleCloseModal = () => {
        setOpen(false);
        setPlayerToTrain('');
    };

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

    const isXs = useMediaQuery(theme.breakpoints.down('sm'));
    const memoizedColumnsScheduled = useMemo(
        () => ScheduleTrainingsColumns(handleSkillCellClick, isXs),
        [isXs],
    );

    return (
        <>
            <TrainingDialog
                open={open}
                handleClose={handleCloseModal}
                skillToTrain={skillToTrain}
                skillUnderTraining={skillUnderTraining}
                playerId={playerToTraing}
            />
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