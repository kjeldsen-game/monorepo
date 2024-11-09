import { CalendarButton } from '@/shared/components/CalendarButton';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import { TFunction } from 'i18next';
import { Moment } from 'moment';
import Link from 'next/link';
import { leftColumnConfig, rightColumnConfig } from './ColumnsConfig';

const leagueColumns = (
  t: TFunction,
  handleChallengeButtonClick: (id: string, date: Moment) => void,
  disabledDates: number[],
): GridColDef[] => [
  {
    field: 'teamName',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Team Name</div>,
    ...leftColumnConfig,
    renderCell: (params: GridCellParams) => (
      <Link
        style={{ paddingInline: '20px' }}
        passHref
        href={`/team/${params.row.id}`}>
        {params.row.name}
      </Link>
    ),
  },
  {
    field: 'manager',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Manager</div>,
    ...rightColumnConfig,
    renderCell: (params: GridCellParams) => (
      <CalendarButton
        datePickerProps={{
          shouldDisableTime: (moment, view) =>
            disabledDates.includes(moment.toDate().getTime()) &&
            view === 'minutes',
          minutesStep: 10,
          openTo: 'day',
        }}
        onDatePick={(date) => handleChallengeButtonClick(params.row.id, date)}>
        {t('challenge.challenge')}
      </CalendarButton>
    ),
  },
];

export { leagueColumns };
export default leagueColumns;
