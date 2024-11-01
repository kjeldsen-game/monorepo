import React from 'react';
import {
    Typography,
    Grid,
    Select,
    MenuItem,
    SelectChangeEvent,
} from '@mui/material';
import { FormValues } from './MarketFilter';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

interface MarketFilterPositionSelectProps {
    formValues: FormValues;
    handleSelectChange: (event: SelectChangeEvent<string>) => void;
    menuValues: any;
}

const MarketFilterPositionSelect: React.FC<MarketFilterPositionSelectProps> = ({
    formValues,
    handleSelectChange,
    menuValues,
}: MarketFilterPositionSelectProps) => {
    return (
        <Grid item maxWidth={'200px'}>
            <Typography
                variant="subtitle1"
                sx={{
                    marginBottom: '8px',
                    opacity: '30%',
                    fontSize: '12px',
                }}>
                Position
            </Typography>
            <Select
                autoWidth
                size="small"
                value={formValues.position}
                onChange={handleSelectChange}
                sx={{
                    backgroundColor: 'white',
                    width: '200px',
                    '&:hover .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#FF3F84',
                    },
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#FF3F84',
                    },
                }}
                inputProps={{
                    style: { backgroundColor: 'white' },
                }}>
                <MenuItem value="">-</MenuItem>
                {Object.values(menuValues).map((menuValue) => (
                    <MenuItem key={String(menuValue)} value={String(menuValue)}>
                        {convertSnakeCaseToTitleCase(String(menuValue))}
                    </MenuItem>
                ))}
            </Select>
        </Grid>
    );
};

export default MarketFilterPositionSelect;
