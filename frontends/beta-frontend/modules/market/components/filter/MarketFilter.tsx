import {
    Box,
    Grid,
} from '@mui/material';
import React from 'react';
import SkillFilterSection from './SkillFilterSection';
import BaseFilterSection from './BaseFilterSection';
import CustomButton from '@/shared/components/Common/CustomButton';
import { useMarketFilterForm } from 'modules/market/hooks/useMarketFilterForm';
import { createAuctionQueryFilter } from 'modules/market/utils/MarketUtils';
import CustomAccordion from '@/shared/components/custom-accordion/CustomAccordion';

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
        <CustomAccordion title='Auction Filter'>
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
        </CustomAccordion>
    );
};

export default MarketFilter;
