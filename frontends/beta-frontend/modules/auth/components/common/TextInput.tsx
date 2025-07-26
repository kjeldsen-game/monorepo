import React from 'react';
import { Controller, Control, RegisterOptions } from 'react-hook-form';
import CustomTextField, { CustomTextFieldProps } from '@/shared/components/Common/CustomTextField';

export type TextInputProps = CustomTextFieldProps & {
    rules?: RegisterOptions;
    control: Control<any>;
}

const TextInput: React.FC<TextInputProps> = ({
    name,
    control,
    rules = {},
    label,
    sx,
    ...rest
}) => {

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
                    type={'text'}
                    sx={{
                        ...sx
                    }}
                    {...rest}
                />
            )}
        />
    );
};

export default TextInput;
