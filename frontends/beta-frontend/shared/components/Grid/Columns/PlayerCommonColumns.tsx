import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { PlayerPositionSelect } from '../../PlayerPositionSelect';
import { Player } from '@/shared/models/Player';
import SelectInput from '@mui/material/Select/SelectInput';
import { positionComparator } from '@/shared/utils/GridUtils';

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
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      sortComparator: positionComparator,
      valueGetter: (params: GridValueGetterParams) => params.row,
      renderCell: (params) => {
        if (isEditing) {
          return (
            // <SelectInput/>
            <PlayerPositionSelect
              onChange={(value) =>
                handlePlayerPositionChange(params.row, value)
              }
              value={
                PlayerPosition[
                  params.row.position as keyof typeof PlayerPosition
                ] ?? undefined
              }
            />
          );
        }

        const position = params.row.position as keyof typeof PlayerPosition;
        const initials = getPositionInitials(position);

        return (
          <div
            style={{
              color: 'black',
              padding: '2px 8px',
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
    },
    ...(skills ? playerSkillsColumns() : []),
  ];

  return columns;
};
