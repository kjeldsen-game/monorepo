import React, { useState } from 'react';
import { Box, Grid, Button, SelectChangeEvent, Collapse } from '@mui/material';
import MarketFilterMinMaxInput from './MarketFilterMinMaxInput';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import MarketButton from './MarketButton';
import { createAuctionQueryFilter } from '@/shared/utils/MarketUtils';
import MarketFilterPositionSelect from './MarketFilterPositionSelect';
import MarketFilterSkillRow from './MarketFilterSkillRow';

const skills = ['SC', 'OP', 'BC', 'PA', 'AE', 'CO', 'TA', 'DP'];

interface MarketFilterProps {
    refetch: (page?: number, size?: number, filter?: string) => void;
}

export interface FormValues {
    playerOffer: object;
    playerAge: object;
    position: string;
    skillRanges: SkillRanges;
}

export interface SkillRanges {
    SC: SkillRange;
    OP: SkillRange;
    BC: SkillRange;
    PA: SkillRange;
    AE: SkillRange;
    CO: SkillRange;
    TA: SkillRange;
    DP: SkillRange;
}

export interface SkillRange {
    from: string;
    to: string;
    potentialFrom: string;
    potentialTo: string;
}

const MarketFilter: React.FC<MarketFilterProps> = ({
    refetch,
}: MarketFilterProps) => {
    const [showPotentialFilters, setShowPotentialFilters] = useState(false);
    const [formValues, setFormValues] = useState({
        playerOffer: { min: '', max: '' },
        playerAge: { from: '', to: '' },
        position: '',
        skillRanges: {
            SC: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            OP: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            BC: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            PA: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            AE: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            CO: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            TA: { from: '', to: '', potentialFrom: '', potentialTo: '' },
            DP: { from: '', to: '', potentialFrom: '', potentialTo: '' },
        },
    });

    const handleSearchButtonClick = () => {
        const params = createAuctionQueryFilter(formValues);
        refetch(1, 10, params);
    };
    const handlePotentialFilterButtonChange = () => {
        setShowPotentialFilters((prev) => !prev);
    };
    const handlePositionChange = (event: SelectChangeEvent<string>) => {
        const { value } = event.target;
        setFormValues((prev) => ({
            ...prev,
            position: value,
        }));
    };
    const handleInputChange = (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => {
        const { name, value } = event.target;
        let mainKey, subKey;
        if (name.includes('Potential')) {
            const parts = ([mainKey, subKey] = name.split(' '));
            [mainKey, subKey] = [
                parts[1].split('.')[0],
                `potential${parts[1].split('.')[1]}`,
            ];
        } else {
            [mainKey, subKey] = name.split('.');
        }
        if (mainKey == 'playerAge' || mainKey == 'playerOffer') {
            setFormValues({
                ...formValues,
                [mainKey]: {
                    ...formValues[mainKey],
                    [subKey.charAt(0).toLowerCase() + subKey.slice(1)]: value,
                },
            });
        } else {
            setFormValues({
                ...formValues,
                skillRanges: {
                    ...formValues.skillRanges,
                    [mainKey as keyof SkillRanges]: {
                        ...formValues.skillRanges[mainKey as keyof SkillRanges],
                        [subKey.charAt(0).toLowerCase() + subKey.slice(1)]:
                            value,
                    },
                },
            });
        }
    };

    return (
        <Box
            sx={{
                padding: '20px',
                borderRadius: '8px',
                background: '#F9F9F9',
            }}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <Grid display={'flex'} justifyContent={'space-between'}>
                        <Grid container spacing={2}>
                            <MarketFilterMinMaxInput
                                formValues={formValues.playerOffer}
                                minLabel={'Min'}
                                maxLabel={'Max'}
                                inputName={'playerOffer'}
                                handleInputChange={handleInputChange}
                            />
                            <MarketFilterMinMaxInput
                                formValues={formValues.playerAge}
                                handleInputChange={handleInputChange}
                                inputName={'playerAge'}
                            />
                            <MarketFilterPositionSelect
                                menuValues={PlayerPosition}
                                formValues={formValues}
                                handleSelectChange={handlePositionChange}
                            />
                        </Grid>
                        <Box
                            display="flex"
                            alignItems={'flex-end'}
                            maxWidth={'200px'}>
                            <MarketButton
                                onClick={handleSearchButtonClick}
                                children={'Search'}
                                sx={{ width: '150px' }}
                            />
                        </Box>
                    </Grid>
                </Grid>
            </Grid>
            <MarketFilterSkillRow
                formValues={formValues}
                handleInputChange={handleInputChange}>
                <Button
                    onClick={handlePotentialFilterButtonChange}
                    disableRipple
                    disableFocusRipple
                    sx={{
                        pt: 6,
                        color: '#FF3F84',
                        fontSize: '12px',
                        textTransform: 'none',
                        '&:hover': {
                            backgroundColor: 'transparent',
                            color: '#FF3F84',
                        },
                    }}>
                    {showPotentialFilters ? 'Hide' : 'Show'} potentials
                </Button>
            </MarketFilterSkillRow>
            <Collapse in={showPotentialFilters}>
                <MarketFilterSkillRow
                    formValues={formValues}
                    handleInputChange={handleInputChange}
                    rowName="Potential  "
                />
            </Collapse>
        </Box>
    );
};

export default MarketFilter;
