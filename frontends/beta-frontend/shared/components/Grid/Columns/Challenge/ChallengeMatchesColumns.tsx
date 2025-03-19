import { CalendarButton } from '@/shared/components/CalendarButton';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { leftColumnConfig, rightColumnConfig } from '../ColumnsConfig';
import { TFunction } from 'next-i18next';
import { Moment } from 'moment';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';
import MarketButton from '@/shared/components/Market/MarketButton';

const challengeMatchesColumns = (
  t: TFunction,
  handleChallengeButtonClick: (id: string) => void,
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
      <MarketButton
        sx={{ marginRight: '8px' }}
        onClick={() => handleChallengeButtonClick(params.row.id)}>
        Challenge
      </MarketButton>
      // <CalendarButton
      //   datePickerProps={{
      //     shouldDisableTime: (moment, view) =>
      //       disabledDates.includes(moment.toDate().getTime()) &&
      //       view === 'minutes',
      //     minutesStep: 10,
      //     openTo: 'day',
      //   }}
      //   onDatePick={(date) => handleChallengeButtonClick(params.row.id, date)}>
      //   Challenge
      // </CalendarButton>
    ),
  },
];

export default challengeMatchesColumns;
