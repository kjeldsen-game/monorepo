import { Box, Grid } from '@mui/material';
import { useState } from 'react';
import BillboardModal from './BillboardModal';
import SponsorsModal from './SponsorsModal';
import { IncomePeriodicity } from '@/shared/models/Economy';
import GeneralEconomyCard from './GeneralEconomyCard';
import BillboardCard from './BillboardCard';
import SponsorCard from './SponsorCard';
import { filterSponsorsByPeriodicity } from '@/shared/utils/EconomyUtils';

interface GeneralEconomyTabViewProps {
  sponsors: any;
  billboardDeal: any;
  balance: number;
  prices: any;
}

const GeneralEconomyTabView: React.FC<GeneralEconomyTabViewProps> = ({
  sponsors,
  billboardDeal,
  balance,
  prices,
}: GeneralEconomyTabViewProps) => {
  const [sponsorType, setSponsorType] = useState<IncomePeriodicity>(0);
  const [openBillboard, setOpenBillboard] = useState<boolean>(false);
  const [openSponsor, setOpenSponsor] = useState<boolean>(false);

  const handleCloseModalSponsor = () => {
    setOpenSponsor(false);
  };

  const handleCloseModalBillboard = () => {
    setOpenBillboard(false);
  };

  const handleOpenSponsorModal = (type: IncomePeriodicity) => {
    setOpenSponsor(true);
    setSponsorType(type);
  };

  return (
    <Box sx={{ width: '100%' }}>
      <BillboardModal
        open={openBillboard}
        handleClose={handleCloseModalBillboard}></BillboardModal>
      <SponsorsModal
        type={sponsorType}
        open={openSponsor}
        handleClose={handleCloseModalSponsor}></SponsorsModal>

      <Grid
        container
        spacing={2}
        sx={{ borderRadius: '8px', background: '#F9F9F9' }}>
        <GeneralEconomyCard balance={balance} pricing={prices} />

        <BillboardCard
          billboardDeal={billboardDeal}
          open={openBillboard}
          setOpen={setOpenBillboard}></BillboardCard>
        <Grid
          item
          xs={12}
          sm={6}
          md={4}
          sx={{
            padding: '20px',
          }}>
          <SponsorCard
            mode={filterSponsorsByPeriodicity(
              sponsors,
              IncomePeriodicity.WEEKLY,
            )}
            handleOpenModal={handleOpenSponsorModal}
            open={openSponsor}
            type={IncomePeriodicity.WEEKLY}
          />
          <Box sx={{ height: '10%' }}></Box>
          <SponsorCard
            mode={filterSponsorsByPeriodicity(
              sponsors,
              IncomePeriodicity.ANNUAL,
            )}
            handleOpenModal={handleOpenSponsorModal}
            open={openSponsor}
            type={IncomePeriodicity.ANNUAL}
          />
        </Grid>
      </Grid>
    </Box>
  );
};

export default GeneralEconomyTabView;
