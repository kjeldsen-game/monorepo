import { MenuItem, Select } from '@mui/material';
import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  baseColumnConfig,
  leftColumnConfig,
  rightColumnConfig,
} from './ColumnsConfig';

const incomingMatchesColumns = (
  handleLineupChange: (value: number, teamId: string) => void,
): GridColDef[] => [
  {
    field: 'away',
    ...leftColumnConfig,
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Away</div>,
    renderCell: (params: GridCellParams) => (
      <Link
        style={{ paddingInline: '20px' }}
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
      <Link passHref href={`/team/${params.row.home.id}`}>
        {params.row.home.name}
      </Link>
    ),
  },
  {
    field: 'dateTime',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Date</div>,
    ...baseColumnConfig,
  },
  {
    field: 'lineupButton',
    renderHeader: () => <div style={{ paddingInline: '20px' }}>Lineup</div>,
    ...rightColumnConfig,
    renderCell: (val) => {
      return (
        <Select
          variant="standard"
          color="info"
          sx={{ mx: '10px', height: '40px' }}
          labelId="demo-simple-select-label"
          id="demo-simple-select"
          label="Lineup"
          value={0}
          onChange={(item) =>
            handleLineupChange(item.target.value as number, val.row.id)
          }>
          <MenuItem value={0}>Default</MenuItem>
          <MenuItem value={1}>Specific lineup...</MenuItem>
        </Select>
      );
    },
  },
];

export { incomingMatchesColumns };
export default incomingMatchesColumns;
