import {
    Accordion,
    AccordionDetails,
    AccordionSummary,
    Box,
    Grid,
} from '@mui/material';
import React from 'react';
import ExpandMoreIcon from '@mui/icons-material/ExpandMore';
import { useMarketFilterForm } from 'hooks/useMarketFilterForm';
import CustomButton from '../../Common/CustomButton';
import { createAuctionQueryFilter } from '@/shared/utils/MarketUtils';
import SkillFilterSection from './SkillFilterSection';
import BaseFilterSection from './BaseFilterSection';

interface MarketFilterProps {
    setFilter: (filter: string) => void;
}

const MarketFilter: React.FC<MarketFilterProps> = ({ setFilter }) => {
    const { formValues, handleInputChange, handlePositionChange } =
        useMarketFilterForm();

    const handleSearchButtonClick = () => {
        console.log("search")
        const query: string = createAuctionQueryFilter(formValues)
        setFilter(query);
    };

    return (
        <Accordion
            defaultExpanded
            sx={{
                boxShadow: 'none',
                background: '#F9F9F9',
                borderRadius: '8px !important',
            }}>
            <AccordionSummary expandIcon={<ExpandMoreIcon />}>
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
