import React, { useState } from 'react';
import { IconButton, InputAdornment } from '@mui/material';
import { Controller, Control, RegisterOptions } from 'react-hook-form';
import CustomTextField, { CustomTextFieldProps } from '@/shared/components/Common/CustomTextField';
import { Visibility } from '@mui/icons-material';

export type PasswordTextFieldProps = CustomTextFieldProps & {
    rules?: RegisterOptions;
    control: Control<any>;
}

const PasswordInput: React.FC<PasswordTextFieldProps> = ({
    name,
    control,
    rules = {},
    type = 'text',
    label,
    sx,
    ...rest
}) => {

    const [showPassword, setShowPassword] = useState(false);

    const tooglePasswordVisibility = () => {
        setShowPassword((prev) => !prev)
    }

    return (
        <Controller
            name={name}
            control={control}
            defaultValue={''}
            rules={{ required: `${label} is required!` }}
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
                <CustomTextField
                    size={'small'}
                    label={label}
                    value={value}
                    onChange={onChange}
                    onBlur={onBlur}
                    error={!!error}
                    helperText={error ? error.message : ''}
                    type={showPassword ? 'text' : 'password'}
                    sx={{
                        ...sx
                    }}
                    slotProps={{
                        input: {
                            endAdornment: (
                                <InputAdornment position="end">
                                    <IconButton
                                        aria-label="toggle password visibility"
                                        onClick={tooglePasswordVisibility}
                                    >
                                        <Visibility />
                                    </IconButton>
                                </InputAdornment>
                            )
                        }
                    }}
                    {...rest}
                />
            )}
        />
    );
};

export default PasswordInput;
