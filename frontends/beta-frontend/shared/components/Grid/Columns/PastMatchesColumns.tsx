import { Button, MenuItem, Select } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from './ColumnsConfig';
import MarketButton from '../../Market/MarketButton';
import { formatDateAndTime } from '@/shared/utils/DateUtils';

const pastMatchesColumns = (
  onReportSelect: (matchId: string) => void,
): GridColDef[] => [
  {
    field: 'away',
    ...leftColumnConfig,
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Away</div>,
    renderCell: (params: GridCellParams) => (
      <Link
        style={{
          paddingInline: '20px',
          color: 'black',
          textDecoration: 'none',
        }}
        passHref
        href={`/team/${params.row.away.id}`}>
        {params.row.away.name}
      </Link>
    ),
  },
  {
    field: 'home',
    ...baseColumnConfig,
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Home</div>,
    renderCell: (params: GridCellParams) => (
      <Link
        style={{
          color: 'black',
          textDecoration: 'none',
        }}
        passHref
        href={`/team/${params.row.home.id}`}>
        {params.row.home.name}
      </Link>
    ),
  },
  {
    field: 'dateTime',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Date</div>,
    ...baseColumnConfig,
    renderCell: (params: GridCellParams) => {
      return formatDateAndTime(params.row?.dateTime);
    },
  },
  {
    field: 'reportButton',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Report</div>,
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

export { pastMatchesColumns };
export default pastMatchesColumns;
