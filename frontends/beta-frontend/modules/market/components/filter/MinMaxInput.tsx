import React from 'react';
import { Grid, Typography, Box } from '@mui/material';
import { MinMaxRange } from 'hooks/useMarketFilterForm';
import MarketInput from './MarketInput';

interface MinMaxInputProps<T extends MinMaxRange> {
    minMaxFormValues: T;
    handleInputChange: (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
    ) => void;
    inputName: string;
    minLabel?: string;
    maxLabel?: string;
}

const MinMaxInput = <T extends MinMaxRange>({
    minMaxFormValues,
    handleInputChange,
    inputName,
    minLabel = 'Min',
    maxLabel = 'Max',
}: MinMaxInputProps<T>) => {

    const isPotential = inputName.includes("Potential");

    return (
        <Grid maxWidth={'120px'}>
            <Typography
                variant="subtitle1"
                sx={{
                    marginBottom: '8px',
                    opacity: '30%',
                    fontSize: '12px',
                }}
            >
                {inputName}
            </Typography>
            <Box display={'flex'}>
                <MarketInput
                    value={isPotential ? minMaxFormValues.potentialMin : minMaxFormValues.min}
                    label={minLabel}
                    name={inputName}
                    onChange={handleInputChange}
                />
                <MarketInput
                    value={isPotential ? minMaxFormValues.potentialMax : minMaxFormValues.max}
                    label={maxLabel}
                    name={inputName}
                    onChange={handleInputChange}
                />
            </Box>
        </Grid>
    );
};

export default MinMaxInput;
