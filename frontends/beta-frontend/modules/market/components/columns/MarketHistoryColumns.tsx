import { GridCellParams, GridColDef } from '@mui/x-data-grid';

import { Box } from '@mui/material';
import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import CustomButton from '@/shared/components/Common/CustomButton';
import { PriceColumn } from './common/PriceColumn';
import { formatDateToDDMMYY } from '@/shared/utils/DateUtils';
import LocalAtmIcon from '@mui/icons-material/LocalAtm';

export const MarketHistoryColumns = (
    handleActionButtonClick: (auction: any) => void,
    teamId: string
) => {
    console.log(teamId)
    const columns: GridColDef[] = [
        PlayerNameColumn((row) => row.player, 'left', (row) => row.teamId === teamId ? LocalAtmIcon : null),
        PriceColumn((row) => row.averageBid, "center", "Average Bid"),
        PriceColumn((row) => row.bid, "center", "My Bid"),
        {
            ...getColumnConfig(),
            field: 'status',
            renderHeader: () => <ColHeader header={'Status'} />,
            renderCell: (params: GridCellParams) => (
                <div>{params.row.status}</div>
            )
        },
        {
            ...getColumnConfig(),
            field: 'endDate',
            renderHeader: () => <ColHeader header={'End Date'} />,
            renderCell: (params: GridCellParams) => (
                <div>{formatDateToDDMMYY(params.row.endedAt)}</div>
            )
        },

        {
            ...getColumnConfig("right"),
            field: "action",
            renderHeader: () => <ColHeader header={"Action"} align={'right'} />,
            renderCell: (params: GridCellParams) => (
                <Box height={'100%'} width={'100%'} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                    <CustomButton
                        variant={'outlined'}
                        sx={{ height: '34px', minWidth: '34px', marginRight: '10px' }}
                        onClick={() => handleActionButtonClick(params.row)}>
                        View Details
                    </CustomButton>
                </Box>

            )
        }
    ];

    return columns;
};
