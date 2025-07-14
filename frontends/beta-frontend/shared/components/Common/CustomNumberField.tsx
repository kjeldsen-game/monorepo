import React from 'react'
import CustomTextField, { CustomTextFieldProps } from './CustomTextField'

export type CustomNumberFieldProps = CustomTextFieldProps & {

}

const CustomNumberField: React.FC<CustomNumberFieldProps> = ({ sx, name, ...rest }) => {
    return (
        <CustomTextField
            name={name}
            type={'number'}
            sx={{
                '& input[type=number]': {
                    MozAppearance: 'textfield',
                },
                '& input[type=number]::-webkit-outer-spin-button, & input[type=number]::-webkit-inner-spin-button':
                {
                    WebkitAppearance: 'none',
                    margin: 0,
                },
                ...sx
            }}
            {...rest}
        />
    )
}

export default CustomNumberField