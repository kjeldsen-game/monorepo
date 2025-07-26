import { FormControl } from '@mui/material';
import CustomNumberField, { CustomNumberFieldProps } from '@/shared/components/Common/CustomNumberField';

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
        </FormControl>
    );
};
export default MarketInput;
