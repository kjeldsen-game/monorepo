import { GridColDef, GridValueGetterParams } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import { TFunction } from 'i18next'

export const simpleTeamColumn = (t: TFunction) => {
  const columns: GridColDef[] = [
    {
      field: 'playerPosition',
      headerName: 'PP',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      valueGetter: (params) => t(`game:playerValues.positionDiminutives.${params.row.position}`),
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
  ]

  return columns
}
