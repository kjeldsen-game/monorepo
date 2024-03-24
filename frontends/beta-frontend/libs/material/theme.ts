import { createTheme } from '@mui/material/styles'
import { red } from '@mui/material/colors'

// Create a theme instance.
export const theme = createTheme({
  typography: {
    fontFamily: 'Lato',
  },
  palette: {
    primary: {
      main: '#3C6997',
    },
    secondary: {
      main: '#FF3F84',
    },
    error: {
      main: red.A400,
    },
  },
})
