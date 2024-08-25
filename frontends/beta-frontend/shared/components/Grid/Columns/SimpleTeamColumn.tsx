import { GridColDef, GridValueGetterParams } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import { PlayerPosition } from '@/shared/models/PlayerPosition'
import { Checkbox } from '@mui/material'
import { PlayerOrder } from '@/shared/models/PlayerOrder'
import { PlayerLineupStatus } from '@/shared/models/PlayerLineupStatus'

export const simpleTeamColumn = () => {
  const columns: GridColDef[] = [
    {
      field: 'playerPosition',
      headerName: 'PP',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      valueGetter: (params) => PlayerPosition[params.row.position as keyof typeof PlayerPosition],
      minWidth: 50,
      flex: 1,
    },
    {
      field: 'DEFENSIVE_POSITIONING',
      renderHeader: () => (
        <div>
          DP<sup>CO</sup>
        </div>
      ),
      minWidth: 50,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return params.row.actualSkills.DEFENSIVE_POSITIONING?.PlayerSkills.actual || params.row.actualSkills.CONTROL?.PlayerSkills.actual
      },
    },
    {
      field: 'BALL_CONTROL',
      renderHeader: () => (
        <div>
          BC<sup>INT</sup>
        </div>
      ),
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 50,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return params.row.actualSkills.BALL_CONTROL?.PlayerSkills.actual || params.row.actualSkills.INTERCEPTIONS?.PlayerSkills.actual
      },
    },
    {
      field: 'SCORE',
      renderHeader: () => (
        <div>
          SC<sup>1on1</sup>
        </div>
      ),
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 50,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return params.row.actualSkills.SCORING?.PlayerSkills.actual || params.row.actualSkills.ONE_ON_ONE?.PlayerSkills.actual
      },
    },
    {
      field: 'PASSING',
      renderHeader: () => (
        <div>
          PA<sup>ORG</sup>
        </div>
      ),
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 50,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return params.row.actualSkills.PASSING?.PlayerSkills.actual || params.row.actualSkills.ORGANIZATION?.PlayerSkills.actual
      },
    },
    {
      field: 'OFFENSIVE_POSITIONING',
      renderHeader: () => (
        <div>
          OP<sup>POS</sup>
        </div>
      ),
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 50,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return params.row.actualSkills.OFFENSIVE_POSITIONING?.PlayerSkills.actual || params.row.actualSkills.POSITIONING?.PlayerSkills.actual
      },
    },
    {
      field: 'TACKLING',
      renderHeader: () => (
        <div>
          TA<sup>RE</sup>
        </div>
      ),
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 50,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return params.row.actualSkills.TACKLING?.PlayerSkills.actual || params.row.actualSkills.REFLEXES?.PlayerSkills.actual
      },
    },
    {
      field: 'CO',
      headerName: 'CO',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 50,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO?.PlayerSkills.actual,
    },
    {
      field: 'playerOrder',
      headerName: 'PO',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      sortable: false,
      valueGetter: (params) => PlayerOrder[params.row.playerOrder as keyof typeof PlayerOrder],
      minWidth: 150,
      flex: 1,
    },
    {
      field: 'playerStatus',
      headerName: 'Status',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      sortable: false,
      renderCell: (params) => <Checkbox color="secondary" checked={params.row.status === PlayerLineupStatus.Active} />,
      minWidth: 20,
      flex: 1,
    },
    // {
    //   field: 'GP',
    //   headerName: 'GP',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'GOALS',
    //   headerName: 'GLs',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'ASSISTS',
    //   headerName: 'As',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'TAKEDOWNS',
    //   headerName: 'Ta',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'CRD',
    //   headerName: 'Crd',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'MOM',
    //   headerName: 'MoM',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'RATING',
    //   headerName: 'Rating',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'playerOrder1',
    //   headerName: 'PO1',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder2',
    //   headerName: 'PO2',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder3',
    //   headerName: 'PO3',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder4',
    //   headerName: 'PO4',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder5',
    //   headerName: 'PO5',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
  ]

  return columns
}
