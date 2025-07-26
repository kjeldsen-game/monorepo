import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import CustomButton from '@/shared/components/Common/CustomButton';
import { Box } from '@mui/material';
import { DateTimeColumn } from './common/DateTimeColumn';
import DoneIcon from '@mui/icons-material/Done';
import CustomIconButton from '@/shared/components/Common/CustomIconButton';
import ClearIcon from '@mui/icons-material/Clear';
import { TeamNameColumn } from '../../columns/TeamNameColumn';

const PendingChallengesColumns = (
    ownTeamId: string,
    onChallengeAccept: (matchID: string) => void,
    onChallengeDecline: (matchID: string) => void,
): GridColDef[] => [
        TeamNameColumn(
            (row) => (row.home.id === ownTeamId ? row.away : row.home),
            'left',
            'Enemy',
        ),
        DateTimeColumn((row) => row?.dateTime, 'center', 'Date'),
        {
            field: 'acceptButton',
            renderHeader: () => <ColHeader header="Action" align={'right'} />,
            ...getColumnConfig('right'),
            renderCell: (params: GridCellParams) => {
                return (
                    <Box sx={{ height: '100%', marginRight: '10px' }} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                        {ownTeamId === params.row?.home?.id ? (
                            <>
                                <CustomButton disabled>
                                    Pending
                                </CustomButton>
                            </>
                        ) : (
                            <>
                                <Box sx={{ height: '100%' }} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                                    <CustomIconButton sx={{ marginRight: '4px' }} onClick={() => onChallengeAccept(params.row?.id)}>
                                        <DoneIcon />
                                    </CustomIconButton>
                                    <CustomIconButton sx={{ marginRight: '4px' }} onClick={() => onChallengeDecline(params.row?.id)}>
                                        <ClearIcon />
                                    </CustomIconButton>
                                </Box>
                            </>
                        )}
                    </Box >
                );
            },
        },
    ];

export default PendingChallengesColumns;
