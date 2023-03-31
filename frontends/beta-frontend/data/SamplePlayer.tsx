import { GridValueGetterParams } from '@mui/x-data-grid'

export type Position = 'DEFENDER' | 'MIDDLE' | 'FORWARD' | 'GOALKEEPER'

export type Skill = 'DEFENSE_POSITION' | 'BALL_CONTROL' | 'SCORE' | 'PASSING' | 'OFFENSIVE_POSITION' | 'TACKLING' | 'CO'

export type Stats = {
  [skill in Skill]: number
}

export interface PlayerStats {
  id: string
  age: number
  name: string
  position: Position
  stats: Stats
}

function getPlayerStats(params: GridValueGetterParams, skill: Skill) {
  return params.row.stats.skill
}

export const samplePlayerColumn = [
  { field: 'name', headerName: 'Name', width: 130 },
  { field: 'age', headerName: 'Age', flex: 1 },
  { field: 'position', headerName: 'Position' },
  {
    field: 'DEFENSE_POSITION',
    headerName: 'Defense Position',
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.DEFENSE_POSITION,
  },
  { field: 'BALL_CONTROL', headerName: 'Ball Control', valueGetter: (params: GridValueGetterParams) => params.row.stats.BALL_CONTROL },
  { field: 'SCORE', headerName: 'Score', flex: 1, valueGetter: (params: GridValueGetterParams) => params.row.stats.SCORE },
  { field: 'PASSING', headerName: 'Passing', flex: 1, valueGetter: (params: GridValueGetterParams) => params.row.stats.PASSING },
  {
    field: 'OFFENSIVE_POSITION',
    headerName: 'Offensive Position',
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.OFFENSIVE_POSITION,
  },
  { field: 'TACKLING', headerName: 'Tackling', flex: 1, valueGetter: (params: GridValueGetterParams) => params.row.stats.TACKLING },
  { field: 'CO', headerName: 'CO', flex: 1, valueGetter: (params: GridValueGetterParams) => params.row.stats.CO },
]

export const samplePlayer: PlayerStats = {
  id: 'player-10',
  age: 32,
  name: 'Devin Gibson',
  position: 'DEFENDER',
  stats: {
    DEFENSE_POSITION: 77,
    BALL_CONTROL: 12,
    SCORE: 10,
    PASSING: 14,
    OFFENSIVE_POSITION: 11,
    TACKLING: 60,
    CO: 19,
  },
}

export const players: PlayerStats[] = [
  samplePlayer,
  {
    id: 'player-1',
    age: 25,
    name: 'John Doe',
    position: 'FORWARD',
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
    position: 'MIDDLE',
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
    position: 'DEFENDER',
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
    position: 'MIDDLE',
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
  {
    id: 'player-5',
    age: 26,
    name: 'Sarah Brown',
    position: 'DEFENDER',
    stats: {
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
    position: 'GOALKEEPER',
    stats: {
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
    position: 'MIDDLE',
    stats: {
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
    position: 'MIDDLE',
    stats: {
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
    position: 'FORWARD',
    stats: {
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
