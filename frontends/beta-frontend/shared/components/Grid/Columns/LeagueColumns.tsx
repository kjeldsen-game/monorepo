import { CalendarButton } from '@/shared/components/CalendarButton'
import { GridCellParams, GridColDef } from '@mui/x-data-grid'
import { GridAlignment } from '@mui/x-data-grid'
import { TFunction } from 'i18next'
import { Moment } from 'moment'
import Link from 'next/link'

const leagueColumns = (t: TFunction, handleChallengeButtonClick: (id: string, date: Moment) => void, disabledDates: number[]): GridColDef[] => [
  {
    field: 'teamName',
    headerName: 'Team name',
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    minWidth: 70,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <Link passHref href={`/team/${params.row.id}`}>
        {params.row.name}
      </Link>
    ),
  },
  {
    field: 'manager',
    headerName: 'Manager',
    headerAlign: 'center' as GridAlignment,
    minWidth: 130,
    flex: 1,
    renderCell: (params: GridCellParams) => (
      <CalendarButton
        datePickerProps={{
          shouldDisableTime: (moment, view) => disabledDates.includes(moment.toDate().getTime()) && view === 'minutes',
          minutesStep: 10,
          openTo: 'day',
        }}
        onDatePick={(date) => handleChallengeButtonClick(params.row.id, date)}>
        {t('challenge.challenge')}
      </CalendarButton>
    ),
  },
]

export { leagueColumns }
export default leagueColumns
