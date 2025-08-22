import React from 'react';
import {
    Box,
    Collapse,
    Grid,
    Typography,
} from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import AddIcon from '@mui/icons-material/Add';
import RemoveIcon from '@mui/icons-material/Remove';
import CustomIconButton from '@/shared/components/Common/CustomIconButton';

interface PricingDataItemProps {
    title: string;
    value: number;
    isEdit: boolean;
    handleAddButton: (title: string, value: number) => void;
    handleRemoveButton: (title: string, value: number) => void;
}

const PricingDataItem: React.FC<PricingDataItemProps> = ({
    title,
    value,
    isEdit,
    handleAddButton,
    handleRemoveButton,
}) => {
    return (
        <Grid size={{ xs: 6, sm: 12, lg: 6 }} textAlign={'center'} sx={{ paddingY: '10px' }}>
            <Typography
                fontSize={20}
                variant="body2"
                sx={{ color: '#54595E99' }}>
                {convertSnakeCaseToTitleCase(title)}:
            </Typography>
            <Typography
                variant="body2"
                sx={{ color: '#A4BC10', paddingY: '5px' }}
                fontSize={16}>
                {value.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                })}{' '}
                $
            </Typography>
            <Collapse in={isEdit}>
                <Box paddingTop={'5'}>
                    <CustomIconButton
                        sx={{ marginX: '5px' }}
                        onClick={() => handleAddButton(title, value)}>
                        <AddIcon />
                    </CustomIconButton>
                    <CustomIconButton
                        sx={{ marginX: '5px' }}
                        onClick={() => handleRemoveButton(title, value)}>
                        <RemoveIcon />
                    </CustomIconButton>
                </Box>
            </Collapse>
        </Grid>
    );
};

export default PricingDataItem;
