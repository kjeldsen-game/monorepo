import { MenuItem, Select } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from './ColumnsConfig';
import { formatDateAndTime } from '@/shared/utils/DateUtils';
import MarketButton from '../../Market/MarketButton';

const incomingMatchesColumns = (
  handleLineupChange: (value: number, teamId: string) => void,
  handlePlayButtonClick: (matchId: string) => void,
  teamId: string,
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
    field: 'actions',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Actions</div>,
    ...rightColumnConfig,
    renderCell: (params: GridCellParams) => {
      return (
        <>
          {params.row.status === 'ACCEPTED' &&
            params.row.home.id === teamId && (
              <MarketButton
                onClick={() => handlePlayButtonClick(params.row.id)}>
                Play
              </MarketButton>
            )}
          <Select
            variant="standard"
            color="info"
            sx={{ mx: '10px', height: '40px' }}
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            label="Lineup"
            value={0}
            onChange={(item) =>
              handleLineupChange(item.target.value as number, params.row.id)
            }>
            <MenuItem value={0}>Default</MenuItem>
            <MenuItem value={1}>Specific lineup...</MenuItem>
          </Select>
        </>
      );
    },
  },
];

export { incomingMatchesColumns };
export default incomingMatchesColumns;
