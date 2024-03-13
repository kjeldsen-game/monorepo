import { Box, Button, Popover } from '@mui/material'
import { StaticDateTimePicker } from '@mui/x-date-pickers'
import { Moment } from 'moment'
import { useRef, useState } from 'react'

interface CalendarButtonProps {
  onDatePick: (date: Moment) => void
  children: React.ReactNode
}

const CalendarButton: React.FC<CalendarButtonProps> = ({ onDatePick, children }) => {
  const [isCalendarOpen, setIsCalendarOpen] = useState<boolean>(false)
  const buttonRef = useRef(null)

  const [selectedDate, setSelectedDate] = useState<Moment | null>(null)

  const handleButtonClick = () => {
    setIsCalendarOpen((old) => !old)
  }

  const handleClose = () => {
    setIsCalendarOpen(false)
  }

  const handleDateChange = (date: Moment | null) => {
    if (!date) return
    setSelectedDate(date)
  }

  const handleConfirm = () => {
    if (selectedDate) {
      onDatePick(selectedDate)
      setIsCalendarOpen(false)
    }
  }

  return (
    <div>
      <Button ref={buttonRef} onClick={handleButtonClick} color="info">
        {children}
      </Button>
      <Popover open={isCalendarOpen} onClose={handleClose} anchorEl={buttonRef.current}>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
          }}>
          <StaticDateTimePicker displayStaticWrapperAs="desktop" openTo="day" onChange={handleDateChange} minutesStep={10} />
          <Button color="info" disabled={selectedDate === null} onClick={handleConfirm}>
            Confirm
          </Button>
        </Box>
      </Popover>
    </div>
  )
}

export { CalendarButton }
