import { Alert, Box, Grid } from '@mui/material';
import { useState } from 'react';
import { IncomePeriodicity } from '@/shared/models/player/Economy';
import { filterSponsorsByPeriodicity } from '@/shared/utils/EconomyUtils';
import BillboardDialog from '../dialogs/BillboardDialog';
import SponsorDialog from '../dialogs/SponsorDialog';
import GeneralEconomyCard from '../cards/GeneralEconomyCard';
import BillboardCard from '../cards/BillboardCard';
import SponsorCard from '../cards/SponsorCard';

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

  const [sponsorType, setSponsorType] = useState<IncomePeriodicity>();
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
      <BillboardDialog
        open={openBillboard}
        handleClose={handleCloseModalBillboard}
      />

      <SponsorDialog
        open={openSponsor}
        type={sponsorType}
        handleClose={handleCloseModalSponsor}
      />

      <Grid
        container
        spacing={2}
        sx={{ borderRadius: 1 }}>
        <GeneralEconomyCard balance={balance} pricing={prices} />
        <BillboardCard
          billboardDeal={billboardDeal}
          setOpen={setOpenBillboard}
        />
        <Grid
          container
          spacing={7}
          size={{ xs: 12, sm: 12, md: 4 }}
          sx={{
            padding: '20px',
            width: '100%'
          }}>
          <Grid size={{ lg: 12, md: 12, sm: 6, xs: 12 }} >
            <SponsorCard
              mode={filterSponsorsByPeriodicity(
                sponsors,
                IncomePeriodicity.WEEKLY,
              )}
              handleOpenModal={handleOpenSponsorModal}
              open={openSponsor}
              type={IncomePeriodicity.WEEKLY}
            />
          </Grid>
          <Grid size={{ lg: 12, md: 12, sm: 6, xs: 12 }}>
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
      </Grid>
    </Box>
  );
};

export default GeneralEconomyTabView;
