import React from 'react';
import { Grid, Typography, Box } from '@mui/material';
import MarketInput from './MarketInput';

interface MarketFilterInputProps {
    formValues: Object;
    handleInputChange: (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    minLabel?: string;
    maxLabel?: string;
    inputName: string;
}

const MarketFilterMinMaxInput: React.FC<MarketFilterInputProps> = ({
    formValues,
    handleInputChange,
    minLabel = 'From',
    maxLabel = 'To',
    inputName,
}) => {
    return (
        <Grid item maxWidth={'120px'}>
            <Typography
                variant="subtitle1"
                sx={{
                    marginBottom: '8px',
                    opacity: '30%',
                    fontSize: '12px',
                }}>
                {inputName}
            </Typography>
            <Box display={'flex'}>
                <MarketInput
                    formValues={formValues[inputName]}
                    handleInputChange={handleInputChange}
                    label={minLabel}
                    inputName={inputName}
                />
                <MarketInput
                    formValues={formValues[inputName]}
                    handleInputChange={handleInputChange}
                    label={maxLabel}
                    inputName={inputName}
                />
            </Box>
        </Grid>
    );
};

export default MarketFilterMinMaxInput;
