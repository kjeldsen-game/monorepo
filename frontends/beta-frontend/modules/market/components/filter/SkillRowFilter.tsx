import React, { ReactNode } from 'react';
import { Box, Grid } from '@mui/material';
import MinMaxInput from './MinMaxInput';
import { SkillKey, SkillRanges } from 'modules/market/types/filterForm';

const OFFENSIVE_SKILLS: SkillKey[] = [
    'SC',
    'OP',
    'BC',
    'PA',
    'AE',
    'CO',
    'TA',
    'DP',
];

interface SkillRowFilterProps {
    formValues: SkillRanges;
    handleInputChange: (
        event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
    ) => void;
    children?: ReactNode;
    namePrefix?: string;
}

const SkillRowFilter: React.FC<SkillRowFilterProps> = ({
    formValues,
    handleInputChange,
    children,
    namePrefix = "",
}: SkillRowFilterProps) => {
    return (
        <Grid container spacing={1} sx={{ justifyContent: 'space-between' }}>
            <Grid size={{ md: 12, sm: 6 }}>
                <Grid
                    container
                    spacing={1}
                    display="flex"
                    justifyContent={{ xs: 'center', md: 'flex-start' }}>
                    {OFFENSIVE_SKILLS.map((skill: SkillKey) => (
                        <Grid size={4} key={skill} my={0} maxWidth={'120px'}>
                            <Box display={'flex'}>
                                <MinMaxInput
                                    minMaxFormValues={formValues[skill]}
                                    handleInputChange={handleInputChange}
                                    inputName={namePrefix + skill}
                                />
                            </Box>
                        </Grid>
                    ))}
                </Grid>
            </Grid>
            <Grid
                size={{ md: 2, sm: 12, xs: 12 }}
                display="flex"
                justifyContent={{ xs: 'center' }}
                alignItems={'end'}>
                {children}
            </Grid>
        </Grid>
    );
};

export default SkillRowFilter;
