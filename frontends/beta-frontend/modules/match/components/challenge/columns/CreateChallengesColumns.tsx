import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import CustomButton from '@/shared/components/Common/CustomButton';
import { Box } from '@mui/material';
import { TeamNameColumn } from '../../columns/TeamNameColumn';

const CreateChallengesColumns = (
    handleChallengeButtonClick: (id: string) => void,
): GridColDef[] => [
        TeamNameColumn((row) => row, "left", "Team Name"),
        {
            field: 'action',
            renderHeader: () => <ColHeader header={'Action'} align={'right'} />,
            ...getColumnConfig('right'),
            renderCell: (params: GridCellParams) => (
                <Box sx={{ height: '100%' }} display={'flex'} justifyContent={'flex-end'} alignItems={'center'}>
                    <CustomButton
                        sx={{ marginRight: '10px' }}
                        onClick={() => handleChallengeButtonClick(params.row.id)}>
                        Challenge
                    </CustomButton>
                </Box >
            ),
        },
    ];

export default CreateChallengesColumns;
