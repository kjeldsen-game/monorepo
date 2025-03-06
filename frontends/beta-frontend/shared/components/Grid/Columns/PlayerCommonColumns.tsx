import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import Link from 'next/link';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { PlayerPositionSelect } from '../../PlayerPositionSelect';
import { Player } from '@/shared/models/Player';
import { positionComparator } from '@/shared/utils/GridUtils';
import PlayerPositionLabel from '../../Player/PlayerPositionLabel';

export const playerCommonColumns = (
  skills: boolean = false,
  isEditing: boolean,
  handlePlayerPositionChange?: (player: Player, value: PlayerPosition) => void,
) => {
  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      renderHeader: () => <div>Name</div>,
      maxWidth: 110,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{ textDecoration: 'none', color: '#000000' }}
          passHref
          href={`/player/${params.row.id}`}>
          {params.row.name}
        </Link>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'age',
      maxWidth: 50,
      renderHeader: () => <div>Age</div>,
      valueGetter: (params: GridValueGetterParams) => params.row.age.years,
    },
    {
      ...baseColumnConfig,
      field: 'playerPosition',
      renderHeader: () => <div>POS</div>,
      sortComparator: positionComparator,
      valueGetter: (params: GridValueGetterParams) => params.row,
      renderCell: (params) => (
        <PlayerPositionLabel
          position={params.row.prefferedPosition as keyof typeof PlayerPosition}
        />
      ),
    },
    {
      ...baseColumnConfig,
      field: 'actualPlayerPosition',
      renderHeader: () => <div>Act. POS</div>,
      sortComparator: positionComparator,
      valueGetter: (params: GridValueGetterParams) => params.row,
      renderCell: (params) => (
        <PlayerPositionLabel
          position={params.row.position as keyof typeof PlayerPosition}
        />
      ),
    },
    ...(skills ? playerSkillsColumns() : []),
  ];

  return columns;
};
