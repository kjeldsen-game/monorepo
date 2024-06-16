export enum PlayerPosition {
  CENTRE_BACK = 'CENTRE_BACK',
  AERIAL_CENTRE_BACK = 'AERIAL_CENTRE_BACK',
  SWEEPER = 'SWEEPER',
  LEFT_BACK = 'LEFT_BACK',
  RIGHT_BACK = 'RIGHT_BACK',
  LEFT_WINGBACK = 'LEFT_WINGBACK',
  RIGHT_WINGBACK = 'RIGHT_WINGBACK',
  DEFENSIVE_MIDFIELDER = 'DEFENSIVE_MIDFIELDER',
  CENTRE_MIDFIELDER = 'CENTRE_MIDFIELDER',
  LEFT_MIDFIELDER = 'LEFT_MIDFIELDER',
  RIGHT_MIDFIELDER = 'RIGHT_MIDFIELDER',
  LEFT_WINGER = 'LEFT_WINGER',
  OFFENSIVE_MIDFIELDER = 'OFFENSIVE_MIDFIELDER',
  RIGHT_WINGER = 'RIGHT_WINGER',
  FORWARD = 'FORWARD',
  AERIAL_FORWARD = 'AERIAL_FORWARD',
  STRIKER = 'STRIKER',
  AERIAL_STRIKER = 'AERIAL_STRIKER',
  GOALKEEPER = 'GOALKEEPER',
}

export enum PlayerPositionColor {
  CENTRE_BACK = '#594fc8',
  AERIAL_CENTRE_BACK = '#6d6394', // Mezcla más clara de #090446 y #786f52
  SWEEPER = '#f7d197', // Desaturación y cambio de luminosidad de #FEB95F
  LEFT_BACK = '#d14d6f', // Mezcla más clara de #f71735 y #c2095a
  RIGHT_BACK = '#cc7da8', // Mezcla más clara de #c2095a y #786f52
  LEFT_WINGBACK = '#5971a6', // Mezcla más clara de #090446 y #f71735
  RIGHT_WINGBACK = '#a8907d', // Mezcla más clara de #786f52 y #c2095a
  DEFENSIVE_MIDFIELDER = '#fbd6a4', // Desaturación y cambio de luminosidad de #FEB95F
  CENTRE_MIDFIELDER = '#e75c7b', // Mezcla más clara de #f71735 y #c2095a
  LEFT_MIDFIELDER = '#985377', // Mezcla más clara de #c2095a y #090446
  RIGHT_MIDFIELDER = '#867e5b', // Mezcla más clara de #090446 y #786f52
  LEFT_WINGER = '#43d9be', // Completamente diferente a los colores de referencia
  OFFENSIVE_MIDFIELDER = '#a7608e', // Mezcla más clara de #f71735 y #786f52
  RIGHT_WINGER = '#fda4a0', // Mezcla más clara de #c2095a y #f71735
  FORWARD = '#3a9db9', // Mezcla más clara de #c2095a y #090446
  AERIAL_FORWARD = '#8497bd', // Mezcla más clara de #090446 y #786f52
  STRIKER = '#fddfb1', // Desaturación y cambio de luminosidad de #FEB95F
  AERIAL_STRIKER = '#b7738e', // Mezcla más clara de #c2095a y #f71735
  GOALKEEPER = '#e1798e', // Mezcla más clara de #f71735 y #c2095a
}
