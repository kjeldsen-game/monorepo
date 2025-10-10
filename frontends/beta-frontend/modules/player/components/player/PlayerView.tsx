import Grid from '@/shared/components/Grid/Grid'
import { sampleRows } from '@/shared/components/Grid/PlayerGrid'
import { Box, Card, Tab, useMediaQuery } from '@mui/material'
import { useMemo } from 'react';
import { PlayerColumns } from './columns/PlayerColumns';
import { theme } from '@/libs/material/theme';
import CustomTabs from '@/shared/components/Tabs/CustomTabs';
import { useTabManager } from '@/shared/hooks/useTabManager';
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import CustomButton from '@/shared/components/Common/CustomButton';
import { usePlayerApi } from 'modules/player/hooks/api/usePlayerApi';
import { useRouter } from 'next/router';
import PlayerDetails from './details/PlayerDetails';
import { PlayerLineupStatus } from '@/shared/models/player/PlayerLineupStatus';
import PlayerOnSaleNotification from './on-sale-notification/PlayerOnSaleNotification';
import SellPlayerConfirmationDialog from './dialog/SellPlayerConfirmationDialog';
import { useModalManager } from '@/shared/hooks/useModalManager';

const PlayerView = () => {

    const isXs = useMediaQuery(theme.breakpoints.down('sm'));
    const memoizedColumns = useMemo(() => PlayerColumns(isXs), [isXs]);

    const playerId = useRouter().query.id as string;
    const { selectedTab, handleTabChange } = useTabManager();
    const { data } = usePlayerApi(playerId);
    const { open, handleCloseModal, setOpen } = useModalManager();
    return (
        <>
            <Box>
                <SellPlayerConfirmationDialog open={open} handleClose={handleCloseModal} playerId={data?.id} />
                <PlayerOnSaleNotification playerId={playerId} />
                <Card sx={{ marginBottom: 2, padding: 2 }}>
                    <PlayerDetails player={data} />
                    <Box mt={1}>
                        <CustomButton
                            disabled
                            sx={{ margin: '0px 8px 0px 0px', padding: 0.5 }}>
                            Main Action
                        </CustomButton>
                        {data && data.status != PlayerLineupStatus.FOR_SALE && (
                            <CustomButton
                                onClick={() => setOpen(true)}
                                sx={{ margin: '0px 8px 0px 0px', padding: 0.5 }}>
                                Sell Player
                            </CustomButton>
                        )}
                        <CustomButton
                            disabled
                            variant={'outlined'}
                            sx={{ margin: '0px 8px 0px 0px', padding: 0.5 }}>
                            Other Action
                        </CustomButton>
                        <CustomButton
                            disabled
                            variant={'outlined'}
                            sx={{ margin: '0px 8px 0px 0px', padding: 0.5 }}>
                            Other Action
                        </CustomButton>
                        <CustomButton
                            disabled
                            variant={'outlined'}
                            sx={{ padding: 0.5 }}>
                            ...
                        </CustomButton>
                    </Box>

                </Card>

                <Card sx={{ background: 'white', padding: 2 }}>
                    <Box
                        sx={{ display: 'flex', justifyContent: 'center', width: '100%' }}
                        paddingY={1}
                        position={'relative'}>
                        <CustomTabs tabs={["Current Season", "Previous Season", "Career"]}
                            selectedTab={selectedTab} handleChange={handleTabChange} />
                    </Box>
                    <Box sx={{ width: '100%' }}>
                        <CustomTabPanel value={selectedTab} index={0}>
                            <Grid hideFooter rows={sampleRows} columns={memoizedColumns} />
                        </CustomTabPanel>
                        <CustomTabPanel value={selectedTab} index={1}>
                            <Grid hideFooter rows={sampleRows} columns={memoizedColumns} />
                        </CustomTabPanel>
                        <CustomTabPanel value={selectedTab} index={2}>
                            <Grid hideFooter rows={sampleRows} columns={memoizedColumns} />
                        </CustomTabPanel>
                    </Box>
                </Card>
            </Box>
        </>
    )
}

export default PlayerView