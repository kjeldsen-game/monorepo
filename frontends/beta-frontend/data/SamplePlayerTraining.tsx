export type Position = 'DEF' | 'MID' | 'FW' | 'GK'

export type Skill = 'DEFENSE_POSITION' | 'BALL_CONTROL' | 'SCORE' | 'PASSING' | 'OFFENSIVE_POSITION' | 'TACKLING' | 'CO'

export type Status = 'HEALTHY' | 'UNHEALTHY' | 'INJURY'

export type Stats = {
  [skill in Skill]: number
}

export interface PlayerStats {
  id: string
  age: number
  name: string
  position: Position
  cs: string
  dv: string
  pb: string
  status: string
  stats: Stats
}

export const samplePlayer: PlayerStats = {
  id: 'player-10',
  age: 32,
  name: 'Devin Gibson',
  status: 'H',
  position: 'DEF',
  cs: 'SC/BC',
  dv: '17 > 18',
  pb: '50%',
  stats: {
    DEFENSE_POSITION: 77,
    BALL_CONTROL: 12,
    SCORE: 10,
    PASSING: 14,
    OFFENSIVE_POSITION: 11,
    TACKLING: 60,
    CO: 40,
  },
}

export const players: PlayerStats[] = [
  samplePlayer,
  {
    id: 'player-1',
    age: 25,
    name: 'John Doe',
    status: 'H',
    position: 'FW',
    cs: 'SC/BC',
    dv: '17 > 18',
    pb: '50%',
    stats: {
      DEFENSE_POSITION: 25,
      BALL_CONTROL: 40,
      SCORE: 8,
      PASSING: 30,
      OFFENSIVE_POSITION: 90,
      TACKLING: 10,
      CO: 12,
    },
  },
  {
    id: 'player-2',
    age: 27,
    name: 'Jane Smith',
    status: 'H',
    position: 'MID',
    cs: 'SC/BC',
    dv: '17 > 18',
    pb: '50%',
    stats: {
      DEFENSE_POSITION: 50,
      BALL_CONTROL: 80,
      SCORE: 3,
      PASSING: 90,
      OFFENSIVE_POSITION: 70,
      TACKLING: 70,
      CO: 30,
    },
  },
  {
    id: 'player-3',
    age: 29,
    name: 'Alex Johnson',
    status: 'H',
    position: 'DEF',
    cs: 'SC/BC',
    dv: '17 > 18',
    pb: '50%',
    stats: {
      DEFENSE_POSITION: 90,
      BALL_CONTROL: 20,
      SCORE: 0,
      PASSING: 20,
      OFFENSIVE_POSITION: 10,
      TACKLING: 90,
      CO: 45,
    },
  },
  {
    id: 'player-4',
    age: 30,
    name: 'David Lee',
    status: 'H',
    position: 'MID',
    cs: 'SC/BC',
    dv: '17 > 18',
    pb: '50%',
    stats: {
      DEFENSE_POSITION: 40,
      BALL_CONTROL: 70,
      SCORE: 5,
      PASSING: 80,
      OFFENSIVE_POSITION: 80,
      TACKLING: 60,
      CO: 25,
    },
  },
]
