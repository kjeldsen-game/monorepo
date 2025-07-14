import { FormControl, TextField, TextFieldProps } from '@mui/material';
import { CUSTOM_TEXT_FIELD_STYLE } from '../../Auth/AuthTextField';
import CustomNumberField, { CustomNumberFieldProps } from '../../Common/CustomNumberField';

type MarketInputProps = CustomNumberFieldProps & {}

const MarketInput: React.FC<MarketInputProps> = ({
    value,
    label,
    name,
    onChange,
}) => {
    return (
        <FormControl size="small">
            <CustomNumberField
                size="small"
                placeholder="-"
                label={label}
                name={`${name}.${label}`}
                onChange={onChange}
                value={value}
                slotProps={{
                    inputLabel: {
                        shrink: true,
                        sx: { color: 'rgba(0, 0, 0, 0.6)' }
                    },
                    input: {
                        sx: {
                            backgroundColor: 'white'
                        }
                    }
                }}
                sx={{
                    paddingY: '0px',
                }}
            />

            {/* <TextField
                type="number"
                size="small"
                variant="outlined"
                placeholder="-"
                label={label}
                name={`${name}.${label}`}
                value={value}
                onChange={onChange}
                slotProps={{
                    inputLabel: {
                        shrink: true,
                        sx: { color: 'rgba(0, 0, 0, 0.6)' }
                    },
                    input: {
                        sx: {
                            backgroundColor: 'white'
                        }
                    }
                }}
                sx={{
                    ...CUSTOM_TEXT_FIELD_STYLE,
                    '& input[type=number]': {
                        MozAppearance: 'textfield',
                    },
                    '& input[type=number]::-webkit-outer-spin-button, & input[type=number]::-webkit-inner-spin-button':
                    {
                        WebkitAppearance: 'none',
                        margin: 0,
                    },
                }}
            /> */}
        </FormControl>
    );
};
export default MarketInput;
