import { Box, CircularProgress, styled } from '@mui/material';
import { useMemo } from 'react';
import { getBackgroundColor } from '@/shared/utils/EconomyUtils';
import Grid from '../../Grid/Grid';
import { economyColumns } from '../../Grid/Columns/Economy/EconomyColumns';

interface TransactionsTabViewProps {
    transactions: any;
}

const TransactionsTabView: React.FC<TransactionsTabViewProps> = ({
    transactions,
}: TransactionsTabViewProps) => {
    const memoizedColumns = useMemo(() => economyColumns(), []);

    const StyledDataGrid = styled(Grid)(({ theme }) => ({
        '& .super-app-theme--TotalIncome': {
            backgroundColor: getBackgroundColor('Total Income').backgroundColor,
            fontWeight: 'bold',
        },
        '& .super-app-theme--TotalOutcome': {
            backgroundColor:
                getBackgroundColor('Total Outcome').backgroundColor,
            fontWeight: 'bold',
        },
        '& .super-app-theme--TotalBalance': {
            backgroundColor:
                getBackgroundColor('Total Balance').backgroundColor,
            fontWeight: 'bold',
        },
    }));

    if (transactions == undefined) return <CircularProgress></CircularProgress>;

    return (
        <Box sx={{ width: '100%' }}>
            {transactions ? (
                <StyledDataGrid
                    getRowClassName={(params) => {
                        const { context } = params.row;
                        if (context === 'Total Income') {
                            return 'super-app-theme--TotalIncome';
                        } else if (context === 'Total Outcome') {
                            return 'super-app-theme--TotalOutcome';
                        } else if (context === 'Total Balance') {
                            return 'super-app-theme--TotalBalance';
                        }
                        return '';
                    }}
                    // sx={{
                    //     fontSize: '14px',
                    //     '& .MuiDataGrid-columnSeparator': {
                    //         display: 'none',
                    //     },
                    //     '& .MuiDataGrid-row': {
                    //         borderBottom: 'none',
                    //     },
                    // }}
                    disableColumnMenu={true}
                    getRowId={(row) => row.context}
                    rows={transactions}
                    columns={memoizedColumns}
                />
            ) : (
                <CircularProgress />
            )}
        </Box>
    );
};

export default TransactionsTabView;
