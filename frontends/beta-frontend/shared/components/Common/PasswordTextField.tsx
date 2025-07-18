import React, { useState } from 'react'
import CustomTextField, { CustomTextFieldProps } from './CustomTextField'
import { IconButton, InputAdornment } from '@mui/material'
import { Visibility } from '@mui/icons-material'

export type PasswordTextFieldProps = CustomTextFieldProps & {

}
const PasswordTextField: React.FC<PasswordTextFieldProps> = ({ sx, name, ...rest }) => {
    const [showPassword, setShowPassword] = useState(false);

    const tooglePasswordVisibility = () => {
        setShowPassword((prev) => !prev)
    }

    return (
        <CustomTextField
            type={showPassword ? 'text' : 'password'}
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
            sx={{
                ...sx
            }}
            {...rest}
        />
    )
}

export default PasswordTextField