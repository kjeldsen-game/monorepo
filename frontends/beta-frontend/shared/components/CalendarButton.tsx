import { Box, Button, Popover } from '@mui/material';
import {
  StaticDateTimePicker,
  StaticDateTimePickerProps,
} from '@mui/x-date-pickers';
import { Moment } from 'moment';
import { useRef, useState } from 'react';

interface CalendarButtonProps {
  onDatePick: (date: Moment) => void;
  children: React.ReactNode;
  datePickerProps?: StaticDateTimePickerProps<Moment> &
    React.RefAttributes<HTMLDivElement>;
}

const CalendarButton: React.FC<CalendarButtonProps> = ({
  onDatePick,
  children,
  datePickerProps,
}) => {
  const [isCalendarOpen, setIsCalendarOpen] = useState<boolean>(false);
  const buttonRef = useRef(null);

  const [selectedDate, setSelectedDate] = useState<Moment | null>(null);

  const handleButtonClick = () => {
    setIsCalendarOpen((old) => !old);
  };

  const handleClose = () => {
    setIsCalendarOpen(false);
  };

  const handleDateChange = (date: Moment | null) => {
    if (!date) return;
    setSelectedDate(date);
  };

  const handleConfirm = () => {
    if (selectedDate) {
      onDatePick(selectedDate);
      setIsCalendarOpen(false);
    }
  };

  return (
    <div>
      <Button
        variant="outlined"
        sx={{
          borderColor: '#FF3F84',
          color: '#FF3F84',
          background: '#FFFFFF',
          '&:hover': {
            backgroundColor: '#FF3F84',
            color: 'white',
            borderColor: 'transparent',
          },
          borderRadius: '4px',
          marginInline: '10px',
        }}
        ref={buttonRef}
        onClick={handleButtonClick}>
        {children}
      </Button>
      <Popover
        open={isCalendarOpen}
        onClose={handleClose}
        anchorEl={buttonRef.current}>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
          }}>
          <StaticDateTimePicker
            displayStaticWrapperAs="desktop"
            onChange={handleDateChange}
            {...datePickerProps}
          />
          <Button
            color="info"
            disabled={selectedDate === null}
            onClick={handleConfirm}>
            Confirm
          </Button>
        </Box>
      </Popover>
    </div>
  );
};

export { CalendarButton };
