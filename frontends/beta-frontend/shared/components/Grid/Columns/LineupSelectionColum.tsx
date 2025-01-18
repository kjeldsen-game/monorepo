import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { Player } from '@/shared/models/Player';
import { baseColumnConfig } from './ColumnsConfig';
import MarketButton from '../../Market/MarketButton';
import CheckIcon from '@mui/icons-material/Check';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { PlayerOrderSelect } from '../../PlayerOrderSelect';
import { PlayerOrder } from '@/shared/models/PlayerOrder';
import CloseIcon from '@mui/icons-material/Close';
import AutorenewIcon from '@mui/icons-material/Autorenew';
import { playerCommonColumns } from './PlayerCommonColumns';

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
    ...playerCommonColumns(true, isEditing, handlePlayerPositionChange),
    {
      ...baseColumnConfig,
      field: 'playerOrder',
      renderHeader: () => <div>Player Order</div>,
      minWidth: 100,
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
      minWidth: 50,
      renderHeader: () => <div>Status</div>,
      valueGetter: (params: GridValueGetterParams) =>
        convertSnakeCaseToTitleCase(params.row.status),
    },
    {
      ...baseColumnConfig,
      field: 'action',
      renderHeader: () => <div>Action</div>,
      renderCell: (params: GridCellParams) => (
        <>
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
          {/* {isEditing && (
            <MarketButton
              sx={{ height: '34px', minWidth: '34px' }}
              children={<CheckIcon />}
              onClick={() => handleActionButtonClick(params.row, player)}
            />
          )} */}
        </>
      ),
    },
  ];

  return columns;
};
