import React from 'react';
import {
    Select,
    MenuItem,
    Typography,
    SxProps,
    Theme,
    SelectProps,
    Box,
} from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

type CustomSelectInputProps = SelectProps & {
    values: any[] | Record<string, any>;
    title?: string;
    hideDefaultOption?: boolean;
    sx?: SxProps<Theme>;
}

const CustomSelectInput: React.FC<CustomSelectInputProps> = ({
    values,
    value,
    title,
    disabled = false,
    hideDefaultOption = false,
    onChange,
    sx,
    ...selectProps
}) => {
    const options = Array.isArray(values)
        ? values
        : Object.values(values);

    return (
        <Box
            height={'100%'}
            display={'flex'}
            flexDirection={'column'}
            justifyContent={'space-between'}
        >
            {title && (
                <Typography
                    variant="subtitle1"
                    sx={{
                        marginBottom: '2px',
                        opacity: '30%',
                        fontSize: '12px',
                    }}
                >
                    {title}
                </Typography>
            )}
            <Select
                size="small"
                value={value ?? ''}
                onChange={onChange}
                disabled={disabled}
                sx={{
                    backgroundColor: 'white',
                    width: '200px',
                    '&:hover .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#FF3F84',
                    },
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                        borderColor: '#FF3F84',
                    },
                    ...sx,
                }}
                inputProps={{
                    style: { backgroundColor: 'white' },
                }}
                {...selectProps}
            >
                {!hideDefaultOption && <MenuItem value="">-</MenuItem>}
                {options.map((menuValue) => (
                    <MenuItem key={String(menuValue)} value={String(menuValue)}>
                        {convertSnakeCaseToTitleCase(String(menuValue))}
                    </MenuItem>
                ))}
            </Select>
        </Box>
    );
};

export default CustomSelectInput;
