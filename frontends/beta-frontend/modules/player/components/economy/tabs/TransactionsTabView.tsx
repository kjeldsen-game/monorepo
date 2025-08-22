import { Box } from '@mui/material';
import { useMemo } from 'react';
import { EconomyColumns } from '../columns/EconomyColumns';
import { Transaction } from 'modules/player/types/Economy';
import { StyledEconomyDatagrid } from '../datagrids/StyledEconomyDatagrid';


interface TransactionsTabViewProps {
    transactions: Transaction[];
    isLoading: boolean
}

const TransactionsTabView: React.FC<TransactionsTabViewProps> = ({
    transactions, isLoading
}: TransactionsTabViewProps) => {
    const memoizedColumns = useMemo(() => EconomyColumns(), []);

    return (
        <Box sx={{ width: '100%' }}>
            <StyledEconomyDatagrid
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
                loading={isLoading}
                hideFooter={true}
                getRowId={(row) => row.context}
                rows={transactions}
                columns={memoizedColumns} />
        </Box>
    );
};

export default TransactionsTabView;
