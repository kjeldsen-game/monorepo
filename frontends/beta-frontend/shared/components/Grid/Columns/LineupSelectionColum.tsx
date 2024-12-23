import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import { PlayerPositionSelect } from '../../PlayerPositionSelect';
import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { Player } from '@/shared/models/Player';
import { baseColumnConfig, leftColumnConfig } from './ColumnsConfig';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import MarketButton from '../../Market/MarketButton';
import CheckIcon from '@mui/icons-material/Check';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { PlayerOrderSelect } from '../../PlayerOrderSelect';
import { PlayerOrder } from '@/shared/models/PlayerOrder';
import CloseIcon from '@mui/icons-material/Close';
import AutorenewIcon from '@mui/icons-material/Autorenew';

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

export const lineupSelectionColumn = (
  isEditing: boolean,
  player: any,
  handleActionButtonClick: (player: Player, player2: Player) => void,
  handlePlayerChange?: (
    value: Player,
    order: PlayerOrder | PlayerPosition,
    field: any,
  ) => void,
) => {
  const handlePlayerOrderChange = (
    player: Player,
    value: PlayerOrder,
  ): void => {
    handlePlayerChange?.(player, value, 'playerOrder');
  };

  const handlePlayerPositionChange = (
    player: Player,
    value: PlayerPosition,
  ): void => {
    handlePlayerChange?.(player, value, 'position');
  };

  const columns: GridColDef[] = [
    {
      ...leftColumnConfig,
      field: 'name',
      renderHeader: () => <div style={{ paddingLeft: '20px' }}>Name</div>,
      minWidth: 130,
      renderCell: (params: GridCellParams) => (
        <div
          style={{
            paddingInline: '20px',
            color: 'black',
            textDecoration: 'none',
          }}>
          {params.row.name}
        </div>
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
      minWidth: 100,
      field: 'playerPosition',
      renderHeader: () => <div>Position</div>,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      renderCell: (params) => {
        if (isEditing) {
          return (
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
              color: '#FFFFFF',
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
      minWidth: 150,
      renderCell: (params) => {
        if (isEditing) {
          return (
            <PlayerOrderSelect
              onChange={(value) => handlePlayerOrderChange(params.row, value)}
              value={
                PlayerOrder[
                  params.row.playerOrder as keyof typeof PlayerOrder
                ] ?? undefined
              }
            />
          );
        }

        const playerOrderValue =
          PlayerOrder[params.row.playerOrder as keyof typeof PlayerOrder] ??
          'N/A';

        return (
          <div style={{ textAlign: 'center', width: '100%' }}>
            {convertSnakeCaseToTitleCase(playerOrderValue)}
          </div>
        );
      },
      valueGetter: (params) =>
        PlayerOrder[params.row.playerOrder as keyof typeof PlayerOrder],
    },
    {
      ...baseColumnConfig,
      field: 'status',
      minWidth: 100,
      renderHeader: () => <div>Status</div>,
      valueGetter: (params: GridValueGetterParams) =>
        convertSnakeCaseToTitleCase(params.row.status),
    },
    {
      ...baseColumnConfig,
      field: 'action',
      renderHeader: () => <div>Action</div>,
      renderCell: (params: GridCellParams) => (
        <MarketButton
          sx={{ height: '34px', minWidth: '34px' }}
          children={
            isEditing ? (
              <CloseIcon />
            ) : player ? (
              <AutorenewIcon />
            ) : (
              <CheckIcon />
            )
          }
          onClick={() => handleActionButtonClick(params.row, player)}
        />
      ),
    },
  ];

  return columns;
};
