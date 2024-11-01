import { Box, CircularProgress, styled } from '@mui/material';
import { useMemo } from 'react';
import Grid from '../Grid/Grid';
import { economyColumns } from '../Grid/Columns/EconomyColumns';
import { getBackgroundColor } from '@/shared/utils/EconomyUtils';
import { playerTransactionsColumns } from '../Grid/Columns/PlayerTransactionsColumns';

interface WagesTabViewProps {
    playerTransactions: any;
}

const WagesTabView: React.FC<WagesTabViewProps> = ({
    playerTransactions,
}: WagesTabViewProps) => {
    const memoizedColumns = useMemo(() => playerTransactionsColumns(), []);
    return (
        <Box sx={{ width: '100%' }}>
            <Grid
                rowHeight={40}
                sx={{
                    fontSize: '14px',
                    '& .MuiDataGrid-columnSeparator': {
                        display: 'none',
                    },
                    '& .MuiDataGrid-row': {
                        borderBottom: 'none',
                    },
                }}
                getRowId={(row) => row.player.id}
                rows={playerTransactions}
                columns={memoizedColumns}
            />
        </Box>
    );
};

export default WagesTabView;
