import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import CustomButton from '@/shared/components/Common/CustomButton';
import { Box } from '@mui/material';
import { DateTimeColumn } from './common/DateTimeColumn';
import { TeamNameColumn } from '../../columns/TeamNameColumn';

const PastChallengesColumns = (
    onReportSelect: (matchId: string) => void,
): GridColDef[] => [
        TeamNameColumn((row) => row.home, "left", "Home"),
        TeamNameColumn((row) => row.away, "center", "Away"),
        DateTimeColumn((row) => row?.dateTime, 'center', 'Date'),
        {
            field: 'reportButton',
            renderHeader: () => <ColHeader header="Report" align={'right'} />,
            ...getColumnConfig('right'),
            renderCell: (params: GridCellParams) => {
                return (
                    <Box sx={{ height: '100%' }} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                        <CustomButton
                            sx={{ marginRight: '10px' }}
                            onClick={() => onReportSelect(params.row.id)}>
                            Report
                        </CustomButton>
                    </Box>
                );
            },
        },
    ];

export default PastChallengesColumns;
