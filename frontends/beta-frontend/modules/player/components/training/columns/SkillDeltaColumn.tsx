import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Box, Typography } from '@mui/material';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import { getArrowColorTrainingDataGrid } from 'modules/player/utils/TrainingUtils';

export const SkillDeltaColumn = (): GridColDef => {
    return {
        ...getColumnConfig(),
        field: "dv",
        renderHeader: () => <ColHeader header={'DV'} />,
        renderCell: (params: GridCellParams) => {
            return (
                <Box sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    height: '100%'
                }}>
                    <Typography>{params.row.pointsBeforeTraining}</Typography>
                    <ArrowRightIcon sx={{ color: getArrowColorTrainingDataGrid(params.row.trainingType) }} />
                    <Typography sx={{ fontWeight: 'bold' }}>
                        {' '}
                        {params.row.pointsAfterTraining}
                    </Typography>
                </Box>
            )
        },
    };
};
