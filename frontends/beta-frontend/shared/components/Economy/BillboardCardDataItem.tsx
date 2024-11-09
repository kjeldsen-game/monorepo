import React from 'react';
import { Grid, SxProps, Typography } from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

interface BillboardCardDataItemProps {
    title: string;
    value: string | number;
    isMoney?: boolean;
    sx?: SxProps;
}

const BillboardCardDataItem: React.FC<BillboardCardDataItemProps> = ({
    title,
    value,
    isMoney = false,
    sx = {},
}) => {
    return (
        <Grid item xs={6} textAlign={'center'} sx={{ paddingY: '20px' }}>
            <Typography
                fontSize={20}
                variant="body2"
                sx={{ color: '#54595E99' }}>
                {title}:
            </Typography>
            <Typography variant="body2" sx={{ color: '#A4BC10' }} fontSize={16}>
                {typeof value === 'string' ? (
                    <>{convertSnakeCaseToTitleCase(value)}</>
                ) : isMoney ? (
                    <>
                        {value.toLocaleString(undefined, {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                        })}{' '}
                        $
                    </>
                ) : (
                    <>{value}</>
                )}
            </Typography>
        </Grid>
    );
};

export default BillboardCardDataItem;
