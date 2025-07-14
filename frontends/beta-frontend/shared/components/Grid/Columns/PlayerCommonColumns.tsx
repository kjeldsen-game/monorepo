import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { baseColumnConfig, leftColumnConfig } from './common/config/ColumnsConfig';
import PlayerPositionLabel from '../../Player/PlayerPositionLabel';
import ColHeader from './common/components/ColHeader';
import ColLink from './common/components/ColLink';
import { formatName } from '@/shared/utils/PlayerUtils';

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
