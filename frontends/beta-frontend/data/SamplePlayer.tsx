export type Position = 'DEF' | 'MID' | 'FW' | 'GK'

export type Skill = 'DEFENSE_POSITION' | 'BALL_CONTROL' | 'SCORE' | 'PASSING' | 'OFFENSIVE_POSITION' | 'TACKLING' | 'CO'

export type Status = 'HEALTHY' | 'UNHEALTHY' | 'INJURY'

export type actualSkills = {
  [skill in Skill]: number
}

export interface PlayerStats {
  id: string
  age: number
  name: string
  position: Position
  status: string
  actualSkills: actualSkills
}

export const samplePlayer: PlayerStats = {
  id: 'player-10',
  age: 32,
  name: 'Devin Gibson',
  status: 'H',
  position: 'DEF',
  actualSkills: {
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
    actualSkills: {
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
    actualSkills: {
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
    actualSkills: {
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
    actualSkills: {
      DEFENSE_POSITION: 40,
      BALL_CONTROL: 70,
      SCORE: 5,
      PASSING: 80,
      OFFENSIVE_POSITION: 80,
      TACKLING: 60,
      CO: 25,
    },
  },
  {
    id: 'player-5',
    age: 26,
    name: 'Sarah Brown',
    status: 'H',
    position: 'DEF',
    actualSkills: {
      DEFENSE_POSITION: 80,
      BALL_CONTROL: 30,
      SCORE: 0,
      PASSING: 40,
      OFFENSIVE_POSITION: 20,
      TACKLING: 80,
      CO: 35,
    },
  },
  {
    id: 'player-6',
    age: 24,
    name: 'Chris Davis',
    status: 'H',
    position: 'GK',
    actualSkills: {
      DEFENSE_POSITION: 95,
      BALL_CONTROL: 10,
      SCORE: 0,
      PASSING: 10,
      OFFENSIVE_POSITION: 5,
      TACKLING: 10,
      CO: 80,
    },
  },
  {
    id: 'player-7',
    age: 28,
    name: 'Emily Wilson',
    status: 'H',
    position: 'MID',
    actualSkills: {
      DEFENSE_POSITION: 30,
      BALL_CONTROL: 90,
      SCORE: 4,
      PASSING: 85,
      OFFENSIVE_POSITION: 60,
      TACKLING: 50,
      CO: 20,
    },
  },
  {
    id: 'player-8',
    age: 29,
    name: 'Jason Williams',
    status: 'H',
    position: 'MID',
    actualSkills: {
      DEFENSE_POSITION: 10,
      BALL_CONTROL: 85,
      SCORE: 6,
      PASSING: 89,
      OFFENSIVE_POSITION: 64,
      TACKLING: 25,
      CO: 18,
    },
  },
  {
    id: 'player-9',
    age: 26,
    name: 'Ryan Johnson',
    status: 'H',
    position: 'FW',
    actualSkills: {
      DEFENSE_POSITION: 6,
      BALL_CONTROL: 70,
      SCORE: 28,
      PASSING: 20,
      OFFENSIVE_POSITION: 96,
      TACKLING: 12,
      CO: 8,
    },
  },
]
