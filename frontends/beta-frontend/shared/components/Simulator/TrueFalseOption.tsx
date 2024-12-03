import React from 'react';
import {
  FormControl,
  FormControlLabel,
  FormLabel,
  Radio,
  RadioGroup,
} from '@mui/material';

interface TrueFalseOptionProps {
  label: string;
  value: boolean | null;
  onChange: (value: boolean) => void;
}

const TrueFalseOption: React.FC<TrueFalseOptionProps> = ({
  label,
  value,
  onChange,
}) => {
  return (
    <FormControl>
      <FormLabel>{label}</FormLabel>
      <RadioGroup
        row
        value={value !== null ? (value ? 'true' : 'false') : ''}
        onChange={(e) => onChange(e.target.value === 'true')}>
        <FormControlLabel value="true" control={<Radio />} label="True" />
        <FormControlLabel value="false" control={<Radio />} label="False" />
      </RadioGroup>
    </FormControl>
  );
};

export default TrueFalseOption;
