import { GridCellParams, GridValueGetterParams } from '@mui/x-data-grid'
import React from 'react'
import { Select, MenuItem, FormControl, SelectChangeEvent, InputLabel, NativeSelect } from '@mui/material'

import { css } from '@emotion/react'
import { BorderRight, BorderRightOutlined } from '@mui/icons-material'

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
  status: string
  stats: Stats
}

export const samplePlayerColumn = [
  { field: 'status', headerName: 'Health', headerAlign: 'center', align: 'center', minWidth: 70, flex: 1 },
  { field: 'name', headerName: 'Name', headerAlign: 'center', align: 'center', minWidth: 130, flex: 1 },
  { field: 'age', headerName: 'Age', headerAlign: 'center', align: 'center', minWidth: 70, flex: 1 },
  { field: 'position', headerName: 'Position', headerAlign: 'center', align: 'center', minWidth: 70, flex: 1 },
  {
    field: 'DEFENSE_POSITION',
    headerName: 'DP',
    minWidth: 50,
    headerAlign: 'center',
    align: 'center',
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.BALL_CONTROL,
    cellClassName: BorderRightOutlined,
  },
  {
    field: 'BALL_CONTROL',
    headerName: 'BC',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.BALL_CONTROL,
  },
  {
    field: 'SCORE',
    headerName: 'SC',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.SCORE,
  },
  {
    field: 'PASSING',
    headerName: 'PA',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.PASSING,
  },
  {
    field: 'OFFENSIVE_POSITION',
    headerName: 'OP',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.OFFENSIVE_POSITION,
  },
  {
    field: 'TACKLING',
    headerName: 'TA',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.TACKLING,
  },
  {
    field: 'CO',
    headerName: 'CO',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'GP',
    headerName: 'GP',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'GOALS',
    headerName: 'GLs',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'ASSISTS',
    headerName: 'As',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'TAKEDOWNS',
    headerName: 'Ta',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'CRD',
    headerName: 'Crd',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'MOM',
    headerName: 'MoM',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'RATING',
    headerName: 'Rating',
    headerAlign: 'center',
    align: 'center',
    minWidth: 50,
    flex: 1,
    valueGetter: (params: GridValueGetterParams) => params.row.stats.CO,
  },
  {
    field: 'playerOrder1',
    headerName: 'PO1',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    minWidth: 70,
    flex: 1,
  },
  {
    field: 'playerOrder2',
    headerName: 'PO2',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    minWidth: 70,
    flex: 1,
  },
  {
    field: 'playerOrder3',
    headerName: 'PO3',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    minWidth: 70,
    flex: 1,
  },
  {
    field: 'playerOrder4',
    headerName: 'PO4',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    minWidth: 70,
    flex: 1,
  },
  {
    field: 'playerOrder5',
    headerName: 'PO5',
    headerAlign: 'center',
    align: 'center',
    renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    minWidth: 70,
    flex: 1,
  },
]

export function PlayerOrderSelect() {
  const [playerOrder, setPlayerOrder] = React.useState('')

  const handleChangePlayerOrder = (event: SelectChangeEvent) => {
    setPlayerOrder(event.target.value as string)
  }

  return (
    <FormControl sx={{ minWidth: 70, marginTop: '16px' }} size="small">
      <InputLabel id="po1-select-label">PO</InputLabel>
      <Select
        labelId="po1-select-label"
        id="playerOrder1-select"
        value={playerOrder}
        label="PO1"
        autoWidth
        onChange={handleChangePlayerOrder}
        sx={{ marginBottom: '1rem' }}>
        <MenuItem value={'playerOrder1'}>playerOrder1</MenuItem>
        <MenuItem value={'playerOrder2'}>playerOrder2</MenuItem>
        <MenuItem value={'playerOrder3'}>playerOrder3</MenuItem>
      </Select>
    </FormControl>
  )
}

export const samplePlayer: PlayerStats = {
  id: 'player-10',
  age: 32,
  name: 'Devin Gibson',
  status: 'H',
  position: 'DEF',
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
    status: 'H',
    position: 'DEF',
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
    status: 'H',
    position: 'GK',
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
    status: 'H',
    position: 'MID',
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
    status: 'H',
    position: 'MID',
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
    status: 'H',
    position: 'FW',
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
