import { Box, CircularProgress } from '@mui/material';
import MarketFilter from './MarketFilter';
import { useMemo, useState } from 'react';
import { Auction } from '@/shared/models/Auction';
import { marketColumn } from '../Grid/Columns/MarketColumn';
import Grid from '../Grid/Grid';
import DashboardLink from '../DashboardLink';
import MarketModal from './MarketModal';

interface MarketProps {
    auctions: Auction[] | undefined;
    refetch: () => void;
}

const MarketView: React.FC<MarketProps> = ({
    auctions,
    refetch,
}: MarketProps) => {
    const [activeAuction, setActiveAuction] = useState('');
    const [open, setOpen] = useState<boolean>(false);

    const handleRowButtonClick = (auctionId: string) => {
        setActiveAuction(auctionId);
        setOpen(true);
    };

    const memoizedColumns = useMemo(
        () => marketColumn(handleRowButtonClick),
        [],
    );

    const handleCloseModal = () => {
        setOpen(false);
    };

    return (
        <Box sx={{ width: '100%' }}>
            <DashboardLink children={'Back to Dashboard'} />
            <MarketModal
                refetch={refetch}
                auctionId={activeAuction}
                open={open}
                handleClose={handleCloseModal}
            />
            <Box
                sx={{
                    marginBottom: '2rem',
                    alignItems: 'center',
                }}>
                <MarketFilter refetch={refetch} />
            </Box>
            <Box sx={{ width: '100%' }}>
                {auctions ? (
                    <Grid
                        sx={{
                            '& .MuiDataGrid-columnSeparator': {
                                display: 'none',
                            },
                        }}
                        getRowId={(row) => row.auctionId}
                        rows={auctions}
                        columns={memoizedColumns}
                    />
                ) : (
                    <CircularProgress />
                )}
            </Box>
        </Box>
    );
};

export default MarketView;
