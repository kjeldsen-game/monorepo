export interface TeamModifiers {
  tactic: Tactic;
  verticalPressure: VerticalPressure;
  horizontalPressure: HorizontalPressure;
}

export enum Tactic {
  DOUBLE_TEAM = 'DOUBLE_TEAM',
  MAN_ON_MAN = 'MAN_ON_MAN',
  ZONE = 'ZONE',
  COUNTER_ATTACK = 'COUNTER_ATTACK',
  POSSESSION_CONTROL = 'POSSESSION_CONTROL',
  TIKA_TAKA = 'TIKA_TAKA',
  WING_PLAY = 'WING_PLAY',
  CATENACCIO = 'CATENACCIO',
  ROUTE_ONE = 'ROUTE_ONE',
  OFFSIDE_TRAP = 'OFFSIDE_TRAP',
}

export enum VerticalPressure {
  MID_PRESSURE = 'MID_PRESSURE',
  LOW_PRESSURE = 'LOW_PRESSURE',
  NO_VERTICAL_FOCUS = 'NO_VERTICAL_FOCUS',
}

export enum HorizontalPressure {
  SWARM_CENTRE = 'SWARM_CENTRE',
  SWARM_FLANKS = 'SWARM_FLANKS',
  NO_HORIZONTAL_FOCUS = 'NO_HORIZONTAL_FOCUS',
}
