import {
  GridCellParams,
  GridColDef,
  GridComparatorFn,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import { PlayerOrderSelect } from '../../PlayerOrderSelect';
import { PlayerPositionSelect } from '../../PlayerPositionSelect';
import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { Player } from '@/shared/models/Player';
import { PlayerOrder } from '@/shared/models/PlayerOrder';
import { PlayerStatusSelect } from '../../PlayerStatusSelect';
import { PlayerLineupStatus } from '@/shared/models/PlayerLineupStatus';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

export const lineupColumn = (
  isEditing?: boolean,
  handlePlayerChange?: (value: Player) => void,
) => {
  const createSkillColumnConfig = (
    field: string,
    headerName: string,
  ): GridColDef => {
    return {
      ...baseColumnConfig,
      field,
      renderHeader: () => <div>{headerName}</div>,
      minWidth: 50,
      valueGetter: (params: GridValueGetterParams) => {
        const actual = params.row.actualSkills[field]?.PlayerSkills.actual || 0;
        const potential =
          params.row.actualSkills[field]?.PlayerSkills.potential || 0;
        return `${actual}/${potential}`;
      },
    };
  };

  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      renderHeader: () => <div style={{ paddingLeft: '20px' }}>Name</div>,
      minWidth: 130,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{
            paddingInline: '20px',
            color: 'black',
            textDecoration: 'none',
          }}
          passHref
          href={`/player/${params.row.id}`}>
          {params.row.name}
        </Link>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'age',
      renderHeader: () => <div>Age</div>,
      valueGetter: (params: GridValueGetterParams) => params.row.age.years,
    },
    {
      ...baseColumnConfig,
      field: 'playerPosition',
      renderHeader: () => <div>Position</div>,
      renderCell: (params) => {
        const position = params.row.position as keyof typeof PlayerPosition;
        const initials = getPositionInitials(position);
        return (
          <div
            style={{
              color: '#FFFFFF',
              padding: '2px 8px 2px 8px',
              width: '42px',
              height: '24px',
              borderRadius: '5px',
              textAlign: 'center',
              background: PlayerPositionColorNew[position],
            }}>
            {initials}
          </div>
        );
      },
      minWidth: 50,
      flex: 1,
    },
    createSkillColumnConfig('SCORING', 'SC'),
    createSkillColumnConfig('OFFENSIVE_POSITIONING', 'OP'),
    createSkillColumnConfig('BALL_CONTROL', 'BC'),
    createSkillColumnConfig('PASSING', 'PA'),
    createSkillColumnConfig('AERIAL', 'AE'),
    createSkillColumnConfig('CONSTITUTION', 'CO'),
    createSkillColumnConfig('TACKLING', 'TA'),
    createSkillColumnConfig('DEFENSIVE_POSITIONING', 'DP'),
    {
      ...baseColumnConfig,
      field: 'playerOrder',
      renderHeader: () => <div>Player Order</div>,
      valueGetter: (params: GridValueGetterParams) =>
        convertSnakeCaseToTitleCase(params.row.playerOrder),
      minWidth: 50,
      flex: 1,
    },
  ];

  return columns;
};
