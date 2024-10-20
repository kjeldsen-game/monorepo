import React, { ReactNode } from 'react';
import { Box, Typography, SxProps, Theme, Grid } from '@mui/material';
import MarketFilterMinMaxInput from './MarketFilterMinMaxInput';
import { FormValues } from './MarketFilter';

const skills = ['SC', 'OP', 'BC', 'PA', 'AE', 'CO', 'TA', 'DP'];

interface MarketFilterSkillRowProps {
    formValues: FormValues;
    handleInputChange: (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    children?: ReactNode;
    rowName?: string;
}

const MarketFilterSkillRow: React.FC<MarketFilterSkillRowProps> = ({
    formValues,
    handleInputChange,
    children,
    rowName = '',
}: MarketFilterSkillRowProps) => {
    return (
        <Grid container spacing={2}>
            <Grid item xs={12}>
                <Grid
                    container
                    spacing={2}
                    sx={{ justifyContent: 'flex-start' }}>
                    {skills.map((skill) => (
                        <Grid key={skill} item my={0} maxWidth={'120px'}>
                            <Box display={'flex'}>
                                <MarketFilterMinMaxInput
                                    formValues={formValues.skillRanges[skill]}
                                    handleInputChange={handleInputChange}
                                    inputName={rowName + skill}
                                />
                            </Box>
                        </Grid>
                    ))}
                    {children}
                </Grid>
            </Grid>
        </Grid>
    );
};

export default MarketFilterSkillRow;
