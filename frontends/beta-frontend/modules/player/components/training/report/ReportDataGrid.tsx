import React, { useMemo } from 'react'
import { styled, useMediaQuery } from "@mui/material";
import { TrainingEventResponse, TrainingType } from "modules/player/types/TrainingResponses";
import { getArrowColorTrainingDataGrid } from "modules/player/utils/TrainingUtils";
import { theme } from '@/libs/material/theme';
import { TrainingColumns } from '../columns/TrainingColumns';
import Grid from '@/shared/components/Grid/Grid';


interface ReportDataGridProps {
    trainings: TrainingEventResponse[];
}

const ReportDataGrid: React.FC<ReportDataGridProps> = ({ trainings }) => {

    const getRowId = (row: TrainingEventResponse) =>
        `${row.player.name} + ${row.skill} + ${row.pointsAfterTraining}`;

    const isXs = useMediaQuery(theme.breakpoints.down("sm"))
    const memoizedColumns = useMemo(() => TrainingColumns(isXs), [isXs]);
    const lastRowIds = useMemo(() => {
        const map = new Map<TrainingType, string>();

        Object.values(TrainingType).forEach((type) => {
            const rowsOfType = trainings.filter((t) => t.trainingType === type);
            if (rowsOfType.length > 0) {
                const lastRow = rowsOfType[rowsOfType.length - 1];
                map.set(type, getRowId(lastRow));
            }
        });

        return map;
    }, [trainings]);

    const StyledDataGrid = styled(Grid)(({ }) => ({
        '& .playerTraining-last': {
            borderBottom: `2px solid ${getArrowColorTrainingDataGrid(TrainingType.PLAYER_TRAINING)}`,
        },
        '& .declineTraining-last': {
            borderBottom: `2px solid ${getArrowColorTrainingDataGrid(TrainingType.DECLINE_TRAINING)}`,
        },
    }));

    return (
        <StyledDataGrid
            data-testid="training-report-grid"
            sx={{
                marginTop: '16px',
                maxHeight: '400px',
                // minHeight: '400px',
            }}
            loading={false}
            getRowId={getRowId}
            rows={trainings}
            columns={memoizedColumns}
            hideFooter
            getRowClassName={(params) => {
                const lastId = lastRowIds.get(params.row.trainingType);
                if (lastId && getRowId(params.row) === lastId) {
                    switch (params.row.trainingType) {
                        case TrainingType.PLAYER_TRAINING:
                            return 'playerTraining-last';
                        case TrainingType.DECLINE_TRAINING:
                            return 'declineTraining-last';
                        default:
                            return '';
                    }
                }
                return '';
            }}
        />
    )
}

export default ReportDataGrid