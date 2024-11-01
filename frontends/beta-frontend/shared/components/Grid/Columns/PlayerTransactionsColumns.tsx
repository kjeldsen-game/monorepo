import {
    PlayerPosition,
    PlayerPositionColor,
} from '@/shared/models/PlayerPosition';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import LocalAtmIcon from '@mui/icons-material/LocalAtm';
import { isNegative } from '@/shared/utils/EconomyUtils';

export const playerTransactionsColumns = () => {
    const columns: GridColDef[] = [
        {
            field: 'playerName',
            renderHeader: () => <div>Player Name</div>,
            headerAlign: 'center' as GridAlignment,
            minWidth: 130,
            flex: 1,
            renderCell: (params: GridCellParams) => (
                <Box
                    display={'flex'}
                    justifyContent={'center'}
                    alignItems={'center'}
                    paddingLeft={'40px'}>
                    {params.row.player.name}
                    {params.row.player.status == 'FOR_SALE' ? (
                        <LocalAtmIcon
                            sx={{ color: '#FF3F84', marginLeft: '20px' }}
                        />
                    ) : (
                        <></>
                    )}
                </Box>
            ),
        },
        {
            field: 'position',
            renderHeader: () => <div>Pos</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            renderCell: (params) => {
                const position = params.row.player
                    .position as keyof typeof PlayerPosition;
                const initials = position
                    .split('_')
                    .map((word) => word.charAt(0).toUpperCase())
                    .join('');

                return (
                    <div
                        style={{
                            color: '#FFFFFF',
                            padding: '2px 8px 2px 8px',
                            width: '42px',
                            height: '24px',
                            borderRadius: '5px',
                            textAlign: 'center',
                            background: PlayerPositionColor[position],
                        }}>
                        {initials}
                    </div>
                );
            },
            minWidth: 50,
            flex: 1,
        },
        {
            field: 'age',
            renderHeader: () => <div>Age</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 70,
            flex: 1,
            renderCell: (params: GridCellParams) => (
                <Box>{params.row.player.age.years}</Box>
            ),
        },
        {
            field: 'thisWeek',
            renderHeader: () => <div>This Week</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'right' as GridAlignment,
            minWidth: 70,
            flex: 1,
            renderCell: (params: GridCellParams) => (
                <Box
                    sx={{
                        paddingRight: '20px',
                        color: isNegative(
                            params.row.transactionSummary.weekSummary,
                        )
                            ? '#C51A1A'
                            : 'black',
                    }}>
                    {params.row.transactionSummary.weekSummary.toLocaleString(
                        undefined,
                        {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                        },
                    )}{' '}
                    $
                </Box>
            ),
        },
        {
            field: 'thisSeason',
            renderHeader: () => <div>This Season</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'right' as GridAlignment,
            minWidth: 70,
            flex: 1,
            renderCell: (params: GridCellParams) => (
                <Box
                    sx={{
                        paddingRight: '20px',
                        color: isNegative(
                            params.row.transactionSummary.seasonSummary,
                        )
                            ? '#C51A1A'
                            : 'black',
                    }}>
                    {' '}
                    {params.row.transactionSummary.seasonSummary.toLocaleString(
                        undefined,
                        {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                        },
                    )}{' '}
                    $
                </Box>
            ),
        },
    ];

    return columns;
};
