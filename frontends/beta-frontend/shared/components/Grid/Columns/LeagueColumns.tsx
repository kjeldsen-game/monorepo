import { CalendarButton } from '@/shared/components/CalendarButton';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import Link from 'next/link';
import { leftColumnConfig, rightColumnConfig } from './ColumnsConfig';
import { TFunction } from 'next-i18next';
import { Moment } from 'moment';

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
        style={{
          paddingInline: '20px',
          color: 'black',
          textDecoration: 'none',
        }}
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
        Challenge
      </CalendarButton>
    ),
  },
];

export { leagueColumns };
export default leagueColumns;
