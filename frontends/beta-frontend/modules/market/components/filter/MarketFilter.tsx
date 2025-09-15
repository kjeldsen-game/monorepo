import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Box,
    Grid,
    Typography,
} from '@mui/material';
import React from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import SkillFilterSection from './SkillFilterSection';
import BaseFilterSection from './BaseFilterSection';
import CustomButton from '@/shared/components/Common/CustomButton';
import { useMarketFilterForm } from 'modules/market/hooks/useMarketFilterForm';
import { createAuctionQueryFilter } from 'modules/market/utils/MarketUtils';
import FilterAltIcon from '@mui/icons-material/FilterAlt';

interface MarketFilterProps {
    setFilter: (filter: string) => void;
}

const MarketFilter: React.FC<MarketFilterProps> = ({ setFilter }) => {
    const { formValues, handleInputChange, handlePositionChange } =
        useMarketFilterForm();

    const handleSearchButtonClick = () => {
        const query: string = createAuctionQueryFilter(formValues)
        setFilter(query);
    };

    return (
        <Accordion
            defaultExpanded={false}
            sx={{
                boxShadow: 'none',
                background: '#F3F4F6',
                borderRadius: '8px !important',
            }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />} >
                <FilterAltIcon sx={{ color: '#555F6C' }} />
                <Typography fontWeight={'bold'} color={"#555F6C"} marginLeft={1}>Auction Filter</Typography>
            </AccordionSummary>
            <AccordionDetails>
                <Grid spacing={1} container>
                    <Grid size={{ md: 9, sm: 12, xs: 12 }}>
                        <BaseFilterSection
                            formValues={formValues}
                            handleInputChange={handleInputChange}
                            handlePositionChange={handlePositionChange}
                        />
                    </Grid>
                    <Grid size={{ md: 3, sm: 12, xs: 12 }}>
                        <Box
                            height="100%"
                            display="flex"
                            justifyContent={{ xs: 'center', sm: 'center', md: 'flex-end' }}
                            alignItems={{ md: 'end' }}>
                            <CustomButton
                                variant="outlined"
                                onClick={handleSearchButtonClick}
                                sx={{
                                    width: {
                                        sm: '70%',
                                        xs: '70%',
                                    },
                                }}>
                                Search
                            </CustomButton>
                        </Box>
                    </Grid>
                </Grid>
                <SkillFilterSection
                    formValues={formValues.skillRanges}
                    handleInputChange={handleInputChange}
                />
            </AccordionDetails>
        </Accordion>
    );
};

export default MarketFilter;
