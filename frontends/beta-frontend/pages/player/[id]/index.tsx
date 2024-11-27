import type { NextPage } from 'next';
import { Box, Button, CircularProgress, Typography } from '@mui/material';
import Grid from '@/shared/components/Grid/Grid';
import { sampleRows, sampleColumns } from '@/shared/components/Grid/PlayerGrid';
import PlayerDetails from '@/shared/components/PlayerDetails';
import { PlayerStats } from '@/data/SamplePlayer';
import { useRouter } from 'next/router';
import PlayerAuctionCard from '@/shared/components/Player/PlayerAuctionCard';
import { useState } from 'react';
import { usePlayerRepository } from '@/pages/api/player/usePlayerRepository';
import { useSession } from 'next-auth/react';
import { useAuctionRepository } from '@/pages/api/market/useAuctionRepository';

const Player: NextPage = () => {
  const { data: userData } = useSession({ required: true });

  let player = {} as PlayerStats;

  const router = useRouter();
  const { id } = router.query;

  const { data, error, isLoading, sellPlayer } = usePlayerRepository(
    id,
    userData?.accessToken,
  );

  const { auctions: auction, refetch } = useAuctionRepository(
    '',
    userData?.accessToken,
    `playerId=${useRouter().query.id}`,
  );

  if (error) return <div>failed to load</div>;
  if (isLoading) return <CircularProgress></CircularProgress>;
  if (data) player = data;

  const handleSellButtonClick = () => {
    sellPlayer();
    setTimeout(() => {
      refetch();
    }, 1000);
  };

  return (
    <>
      <Box>
        <PlayerAuctionCard auction={auction} />
        <PlayerDetails player={player} />
        <Box sx={{ marginBottom: '1rem' }}>
          <Button
            variant="contained"
            color="secondary"
            sx={{ marginRight: '8px', marginBottom: '8px' }}>
            <strong>Main Action</strong>
          </Button>
          {auction != undefined && auction.length > 0 ? (
            <></>
          ) : (
            <Button
              variant="outlined"
              color="secondary"
              sx={{ marginRight: '8px', marginBottom: '8px' }}
              onClick={handleSellButtonClick}>
              <strong>Sell Player</strong>
            </Button>
          )}

          <Button
            variant="outlined"
            color="secondary"
            sx={{ marginRight: '8px', marginBottom: '8px' }}>
            <strong>Other Action</strong>
          </Button>
          <Button
            variant="outlined"
            color="secondary"
            sx={{ marginRight: '8px', marginBottom: '8px' }}>
            <strong>Other Action</strong>
          </Button>
          <Button
            variant="outlined"
            color="secondary"
            sx={{ marginRight: '8px', marginBottom: '8px' }}>
            <strong>...</strong>
          </Button>
        </Box>

        <Typography sx={{ marginBottom: '1rem', borderBottom: '1px solid' }}>
          Current Season
        </Typography>
        <Grid rows={sampleRows} columns={sampleColumns} />
        <Typography
          sx={{
            marginBottom: '1rem',
            marginTop: '2rem',
            borderBottom: '1px solid',
          }}>
          Previous Season
        </Typography>
        <Grid rows={sampleRows} columns={sampleColumns} />
        <Typography
          sx={{
            marginBottom: '1rem',
            marginTop: '2rem',
            borderBottom: '1px solid',
          }}>
          Aggregate
        </Typography>
        <Grid rows={sampleRows} columns={sampleColumns} />
      </Box>
    </>
  );
};

export default Player;
