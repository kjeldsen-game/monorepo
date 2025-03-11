import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from '../ColumnsConfig';
import MarketButton from '../../../Market/MarketButton';
import { formatDateAndTime } from '@/shared/utils/DateUtils';
import ColHeader from '../Common/ColHeader';
import ColLink from '../Common/ColLink';

const pastMatchesColumns = (
  onReportSelect: (matchId: string) => void,
): GridColDef[] => [
  {
    field: 'away',
    ...leftColumnConfig,
    renderHeader: () => <ColHeader header={'Away'} align={'left'} />,
    renderCell: (params: GridCellParams) => (
      <ColLink
        children={params.row.away.name}
        urlValue={`/team/${params.row.away.id}`}
      />
    ),
  },
  {
    field: 'home',
    ...baseColumnConfig,
    renderHeader: () => <ColHeader header={'Home'} />,
    renderCell: (params: GridCellParams) => (
      <ColLink
        children={params.row.home.name}
        urlValue={`/team/${params.row.home.id}`}
      />
    ),
  },
  {
    field: 'dateTime',
    renderHeader: () => <ColHeader header={'Date'} />,
    ...baseColumnConfig,
    renderCell: (params: GridCellParams) => {
      return formatDateAndTime(params.row?.dateTime);
    },
  },
  {
    field: 'reportButton',
    renderHeader: () => <ColHeader header="Report" align={'right'} />,
    ...rightColumnConfig,
    renderCell: (params: GridCellParams) => {
      return (
        <MarketButton
          sx={{ marginInline: '10px' }}
          onClick={() => onReportSelect(params.row.id)}>
          Report
        </MarketButton>
      );
    },
  },
];

export default pastMatchesColumns;
