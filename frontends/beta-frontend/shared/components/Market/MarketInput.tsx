import { FormControl, TextField } from '@mui/material';

interface MarketInput {
  formValues: String;
  handleInputChange: (
    event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>,
  ) => void;
  label: string;
  inputName: string;
}

const MarketInput: React.FC<MarketInput> = ({
  formValues,
  handleInputChange,
  label,
  inputName,
}) => {
  return (
    <FormControl size="small">
      <TextField
        type="number"
        size="small"
        variant="outlined"
        placeholder="-"
        label={label}
        name={`${inputName}.${label}`}
        value={formValues}
        onChange={handleInputChange}
        InputLabelProps={{
          shrink: true,
          sx: {
            color: 'rgba(0, 0, 0, 0.6)',
          },
        }}
        InputProps={{
          style: {
            backgroundColor: 'white',
          },
        }}
        sx={{
          '& .MuiOutlinedInput-root': {
            '& fieldset': {
              borderColor: 'rgba(0, 0, 0, 0.3)',
            },
            '&:hover fieldset': {
              borderColor: '#FF3F84',
            },
            '&.Mui-focused fieldset': {
              borderColor: '#FF3F84',
            },
          },
          '& input[type=number]': {
            MozAppearance: 'textfield',
          },
          '& input[type=number]::-webkit-outer-spin-button, & input[type=number]::-webkit-inner-spin-button':
            {
              WebkitAppearance: 'none',
              margin: 0,
            },
        }}
      />
    </FormControl>
  );
};
export default MarketInput;
