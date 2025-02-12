import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box, MenuItem, Select, Typography } from '@mui/material';
import React from 'react';

interface SelectInputProps {
  values: any;
  value: any;
  title: string;
  handleChange: (...args: any[]) => void;
  disabled?: boolean;
  hideDefaultOption?: boolean;
}

const SelectInput: React.FC<SelectInputProps> = ({
  values,
  value,
  title,
  handleChange,
  disabled = false,
  hideDefaultOption = false,
}) => {
  return (
    <>
      <Typography
        variant="subtitle1"
        sx={{
          marginBottom: '2px',
          opacity: '30%',
          fontSize: '12px',
        }}>
        {title}
      </Typography>
      <Select
        autoWidth
        size="small"
        value={value}
        onChange={handleChange}
        disabled={disabled}
        sx={{
          backgroundColor: 'white',
          width: '200px',
          '&:hover .MuiOutlinedInput-notchedOutline': {
            borderColor: '#FF3F84',
          },
          '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
            borderColor: '#FF3F84',
          },
        }}
        inputProps={{
          style: { backgroundColor: 'white' },
        }}>
        {!hideDefaultOption && <MenuItem value="">-</MenuItem>}
        {Object.values(values).map((menuValue) => (
          <MenuItem key={String(menuValue)} value={String(menuValue)}>
            {convertSnakeCaseToTitleCase(String(menuValue))}
          </MenuItem>
        ))}
      </Select>
    </>
  );
};

export default SelectInput;
