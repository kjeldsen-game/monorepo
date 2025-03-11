import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { positionComparator } from '@/shared/utils/GridUtils';
import PlayerPositionLabel from '../../Player/PlayerPositionLabel';
import ColHeader from './Common/ColHeader';
import ColLink from './Common/ColLink';

export const playerCommonColumns = (
  skills: boolean = false,
  showPotential: boolean = true,
  showPosition: boolean = false,
) => {
  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      minWidth: 80,
      renderHeader: () => <ColHeader header={'Name'} align={'left'} />,
      renderCell: (params: GridCellParams) => (
        <ColLink urlValue={`/player/${params.row.id}`}>
          {params.row.name}
        </ColLink>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'age',
      maxWidth: 50,
      renderHeader: () => <ColHeader header={'Age'} />,
      valueGetter: (params: GridValueGetterParams) => params.row.age.years,
    },
    ...(showPosition
      ? [
          {
            ...baseColumnConfig,
            field: 'playerPosition',
            renderHeader: () => <ColHeader header={'Pos'} />,
            sortComparator: positionComparator,
            valueGetter: (params: GridValueGetterParams) => params.row,
            renderCell: (params: GridCellParams) => (
              <PlayerPositionLabel
                position={
                  params.row.prefferedPosition as keyof typeof PlayerPosition
                }
              />
            ),
          },
        ]
      : []),
    {
      ...baseColumnConfig,
      field: 'actualPlayerPosition',
      renderHeader: () => <ColHeader header={'Act. Pos'} />,
      sortComparator: positionComparator,
      valueGetter: (params: GridValueGetterParams) => params.row,
      renderCell: (params: GridCellParams) => (
        <PlayerPositionLabel
          position={params.row.position as keyof typeof PlayerPosition}
        />
      ),
    },
    ...(skills ? playerSkillsColumns(showPotential) : []),
  ];

  return columns;
};
