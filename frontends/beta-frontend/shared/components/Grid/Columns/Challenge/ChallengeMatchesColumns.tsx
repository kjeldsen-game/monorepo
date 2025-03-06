import { CalendarButton } from '@/shared/components/CalendarButton';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { leftColumnConfig, rightColumnConfig } from '../ColumnsConfig';
import { TFunction } from 'next-i18next';
import { Moment } from 'moment';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';

const challengeMatchesColumns = (
  t: TFunction,
  handleChallengeButtonClick: (id: string, date: Moment) => void,
  disabledDates: number[],
): GridColDef[] => [
  {
    field: 'teamName',
    renderHeader: () => <ColHeader header={'Team Name'} align={'left'} />,
    ...leftColumnConfig,
    renderCell: (params: GridCellParams) => (
      <ColLink children={params.row.name} urlValue={`/team/${params.row.id}`} />
    ),
  },
  {
    field: 'manager',
    renderHeader: () => <ColHeader header={'Manager'} align={'right'} />,
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

export default challengeMatchesColumns;
