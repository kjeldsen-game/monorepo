import React from 'react';
import { TextField } from '@mui/material';
import { Controller, Control, RegisterOptions } from 'react-hook-form';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

interface AuthTextFieldv2Props {
    name: string;
    control: Control<any>;
    defaultValue?: string;
    rules?: RegisterOptions;
    type?: string;
}

export const CUSTOM_TEXT_FIELD_STYLE = {
    '& input:-webkit-autofill': {
        WebkitBoxShadow: '0 0 0 100px white inset !important',
        WebkitTextFillColor: '#000 !important',
        transition: 'background-color 5000s ease-in-out 0s',
    },
    '& label': {
        color: '#00000099',
    },
    '& label.Mui-focused': {
        color: '#00000099',
    },
    '& .MuiOutlinedInput-root': {
        '& fieldset': {
            borderColor: 'grey',
            borderWidth: 1,
        },
        '&:hover fieldset': {
            borderColor: '#FF3F84',
            borderWidth: 1,
        },
        '&.Mui-focused fieldset': {
            borderColor: '#FF3F84',
            borderWidth: 1,
        },
    },
    '& .MuiInputBase-input': {
        color: 'black',
    },
};


const AuthTextField: React.FC<AuthTextFieldv2Props> = ({
    name,
    control,
    defaultValue = '',
    rules = {},
    type = 'text',
}) => {
    return (
        <Controller
            name={name}
            control={control}
            defaultValue={defaultValue}
            rules={rules}
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
                <TextField
                    label={convertSnakeCaseToTitleCase(name)}
                    variant="outlined"
                    value={value}
                    onChange={onChange}
                    onBlur={onBlur}
                    error={!!error}
                    helperText={error ? error.message : ''}
                    type={type}
                    fullWidth
                    sx={{
                        ...CUSTOM_TEXT_FIELD_STYLE,
                        paddingBottom: '8px',
                    }}
                />
            )}
        />
    );
};

export default AuthTextField;
