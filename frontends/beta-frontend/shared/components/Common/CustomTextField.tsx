import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils'
import { TextField, TextFieldProps } from '@mui/material'
import React from 'react'

export type CustomTextFieldProps = TextFieldProps & {

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
        backgroundColor: 'white'
    },
};


const CustomTextField: React.FC<CustomTextFieldProps> = ({ name, value, type = "text", sx, ...rest }) => {
    return (
        <TextField
            name={name}
            label={convertSnakeCaseToTitleCase(name)}
            variant="outlined"
            value={value}
            type={type}
            fullWidth
            sx={{
                ...CUSTOM_TEXT_FIELD_STYLE,
                ...sx
            }}
            {...rest}
        />
    )
}

export default CustomTextField