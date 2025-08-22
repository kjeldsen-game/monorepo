import { Box, Typography } from '@mui/material';
import { useMemo } from 'react';
import { PlayerTransactionsColumns } from '../columns/PlayerTransactionsColumns';
import Grid from '@/shared/components/Grid/Grid';
import { PlayerWage } from 'modules/player/types/Economy';


interface WagesTabViewProps {
  playerTransactions: PlayerWage[];
  isLoading: boolean
}

const WagesTabView: React.FC<WagesTabViewProps> = ({
  playerTransactions, isLoading
}: WagesTabViewProps) => {

  const memoizedColumns = useMemo(() => PlayerTransactionsColumns(), []);

  return (
    <Box sx={{ width: '100%' }}>
      <Grid
        loading={isLoading}
        hideFooter={true}
        getRowId={(row) => row.player.id}
        rows={playerTransactions}
        columns={memoizedColumns}
      />
    </Box>
  );
};

export default WagesTabView;
