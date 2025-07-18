import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { playerSkillsColumns } from './PlayerSkillsColumns';
import { baseColumnConfig, leftColumnConfig } from './common/config/ColumnsConfig';
import { positionComparator } from '@/shared/utils/GridUtils';
import PlayerPositionLabel from '../../Player/PlayerPositionLabel';
import ColHeader from './common/components/ColHeader';
import ColLink from './common/components/ColLink';
import { formatName } from '@/shared/utils/PlayerUtils';
import PlayerStatsTooltip from '../../MatchReport/Tooltips/PlayerStatsTooltip';

export const matchReportPlayercolumns = (
  skills: boolean = false,
  showPotential: boolean = true,
  showPosition: boolean = false,
  truncateName: boolean = false,
  stats: any,
) => {
  // console.log(stats);

  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      minWidth: 80,
      pinnable: true,
      renderHeader: () => <ColHeader header={'Name'} align={'left'} />,
      renderCell: (params: GridCellParams) => {
        const playerStats = stats?.[params.row.id] || null;

        return (
          <PlayerStatsTooltip stats={playerStats}>
            <ColLink urlValue={`/player/${params.row.id}`}>
              {truncateName ? formatName(params.row.name) : params.row.name}
            </ColLink>
          </PlayerStatsTooltip>
        );
      },
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
