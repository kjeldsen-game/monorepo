import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { positionComparator } from '@/shared/utils/GridUtils';
import PlayerPositionLabel from '../../Player/PlayerPositionLabel';
import ColHeader from './Common/ColHeader';
import ColLink from './Common/ColLink';
import { formatName } from '@/shared/utils/PlayerUtils';
import { PlayerAge } from '@/shared/models/player/Player';

export const playerCommonColumns = (
  skills: boolean = false,
  showPotential: boolean = true,
  showPosition: boolean = false,
  truncateName: boolean = false,
) => {
  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      minWidth: 80,
      renderHeader: () => <ColHeader header={'Name'} align={'left'} />,
      renderCell: (params: GridCellParams) => (
        <ColLink urlValue={`/player/${params.row.id}`}>
          {truncateName ? formatName(params.row.name) : params.row.name}
        </ColLink>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'age',
      maxWidth: 50,
      renderHeader: () => <ColHeader header={'Age'} />,
      valueGetter: (params: PlayerAge) => {
        return params.years;
      },
    },
    ...(showPosition
      ? [
          {
            ...baseColumnConfig,
            field: 'playerPosition',
            renderHeader: () => <ColHeader header={'Pos'} />,
            // sortComparator: positionComparator,
            renderCell: (params: GridCellParams) => (
              <PlayerPositionLabel
                position={
                  params.row.preferredPosition as keyof typeof PlayerPosition
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
