import { createTheme } from '@mui/material/styles';
import { red } from '@mui/material/colors';

declare module '@mui/material/styles' {
  interface Palette {
    tertiary: Palette['primary'];
    quaternary: Palette['primary'];
  }
  interface PaletteOptions {
    tertiary?: PaletteOptions['primary'];
    quaternary?: PaletteOptions['primary'];
  }
}

// Create a theme instance.
export const theme = createTheme({
  typography: {
    fontFamily: 'Lato',
  },
  palette: {
    primary: {
      main: '#ffffffff',
    },
    secondary: {
      main: '#FF3F84',
    },
    tertiary: {
      main: '#F3F4F6',
      light: '#E2E8F0'
    },
    quaternary: {
      main: '#555F6C',
    },
    error: {
      main: red.A400,
    },
  },
});
