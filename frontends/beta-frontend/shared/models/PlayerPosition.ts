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

export enum PlayerPositionAbbreviation {
  CENTRE_BACK = 'CB',
  AERIAL_CENTRE_BACK = 'ACB',
  SWEEPER = 'SWP',
  LEFT_BACK = 'LB',
  RIGHT_BACK = 'RB',
  LEFT_WINGBACK = 'LWB',
  RIGHT_WINGBACK = 'RWB',
  DEFENSIVE_MIDFIELDER = 'DM',
  CENTRE_MIDFIELDER = 'CM',
  LEFT_MIDFIELDER = 'LM',
  RIGHT_MIDFIELDER = 'RM',
  LEFT_WINGER = 'LW',
  OFFENSIVE_MIDFIELDER = 'OM',
  RIGHT_WINGER = 'RW',
  FORWARD = 'FW',
  AERIAL_FORWARD = 'AFW',
  STRIKER = 'ST',
  AERIAL_STRIKER = 'AST',
  GOALKEEPER = 'GK',
}

// export enum PlayerPositionColorNew {
//   CENTRE_BACK = '#a3b3b8', // darker green
//   AERIAL_CENTRE_BACK = '#a3b3b8', // darker green
//   SWEEPER = '#a3b3b8', // darker green
//   LEFT_BACK = '#8b9f91', // darker green3
//   RIGHT_BACK = '#8b9f91', // darker green3
//   LEFT_WINGBACK = '#8b9f91', // darker red3
//   RIGHT_WINGBACK = '#8b9f91', // darker red3
//   DEFENSIVE_MIDFIELDER = '#e1b3b3', // darker red
//   CENTRE_MIDFIELDER = '#e1b3b3', // darker red
//   LEFT_MIDFIELDER = '#d38080', // darker red3
//   RIGHT_MIDFIELDER = '#d38080', // darker red3
//   LEFT_WINGER = '#d38080', // darker blue 3
//   OFFENSIVE_MIDFIELDER = '#b0b8e3', // darker blue
//   RIGHT_WINGER = '#d38080', // darker blue 3
//   FORWARD = '#b0b8e3', // darker blue
//   AERIAL_FORWARD = '#b0b8e3', // darker blue
//   STRIKER = '#b0b8e3', // darker blue
//   AERIAL_STRIKER = '#b0b8e3', // darker blue
//   GOALKEEPER = '#e6cc99', // darker yellow
// }

export enum PlayerPositionColorNew {
  CENTRE_BACK = '#d0e0e3', // green
  AERIAL_CENTRE_BACK = '#d0e0e3', // green
  SWEEPER = '#d0e0e3', // green
  LEFT_BACK = '#ABCAA9', // green3
  RIGHT_BACK = '#ABCAA9', // green3
  LEFT_WINGBACK = '#ABCAA9', // red3
  RIGHT_WINGBACK = '#ABCAA9', // red3
  DEFENSIVE_MIDFIELDER = '#f4cccc', // red
  CENTRE_MIDFIELDER = '#f4cccc', // red
  LEFT_MIDFIELDER = '#E99898', //red3
  RIGHT_MIDFIELDER = '#E99898', // red3
  LEFT_WINGER = '#E99898', // blue 3
  OFFENSIVE_MIDFIELDER = '#CCDCFC', // blue
  RIGHT_WINGER = '#E99898', //blue 3
  FORWARD = '#CCDCFC', // blue
  AERIAL_FORWARD = '#CCDCFC', // blue
  STRIKER = '#CCDCFC', // blue
  AERIAL_STRIKER = '#CCDCFC', // blue
  GOALKEEPER = '#fff2cc', // yellow
}

export const TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS: PlayerPosition[] = [
  PlayerPosition.GOALKEEPER,
];

export const TABLE_PLAYER_POSITION_ORDER_DEFENDERS: PlayerPosition[] = [
  PlayerPosition.LEFT_BACK,
  PlayerPosition.LEFT_WINGBACK,
  PlayerPosition.CENTRE_BACK,
  PlayerPosition.RIGHT_WINGBACK,
  PlayerPosition.RIGHT_BACK,
  PlayerPosition.AERIAL_CENTRE_BACK,
  PlayerPosition.SWEEPER,
];

export const TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS: PlayerPosition[] = [
  PlayerPosition.LEFT_MIDFIELDER,
  PlayerPosition.LEFT_WINGER,
  PlayerPosition.DEFENSIVE_MIDFIELDER,
  PlayerPosition.CENTRE_MIDFIELDER,
  PlayerPosition.OFFENSIVE_MIDFIELDER,
  PlayerPosition.RIGHT_WINGER,
  PlayerPosition.RIGHT_MIDFIELDER,
];

export const TABLE_PLAYER_POSITION_ORDER_FORWARDS: PlayerPosition[] = [
  PlayerPosition.FORWARD,
  PlayerPosition.AERIAL_FORWARD,
  PlayerPosition.STRIKER,
  PlayerPosition.AERIAL_STRIKER,
];

export const TABLE_PLAYER_POSITION_ORDER: PlayerPosition[] = [
  ...TABLE_PLAYER_POSITION_ORDER_GOALKEEPERS,
  ...TABLE_PLAYER_POSITION_ORDER_DEFENDERS,
  ...TABLE_PLAYER_POSITION_ORDER_MIDFIELDERS,
  ...TABLE_PLAYER_POSITION_ORDER_FORWARDS,
];
