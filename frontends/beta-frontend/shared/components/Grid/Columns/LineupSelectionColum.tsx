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
import SelectInput from '../../Common/SelectInput';
import { PITCH_AREAS } from '@/shared/models/PitchArea';
import { MenuItem, Select } from '@mui/material';

export const lineupSelectionColumn = (
  isEditing: boolean,
  player: any,
  handleActionButtonClick: (player: Player, player2: Player) => void,
  handlePlayerChange?: (
    value: Player,
    order: PlayerOrder | PlayerPosition | any,
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

  const handlePlayerOrderSpecChange = (player: Player, value: any): void => {
    handlePlayerChange?.(player, value, 'playerOrderDestinationPitchArea');
  };

  const columns: GridColDef[] = [
    ...playerCommonColumns(true, isEditing, handlePlayerPositionChange),
    {
      ...baseColumnConfig,
      field: 'playerOrder',
      renderHeader: () => <div>PO</div>,
      minWidth: 80,
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
    ...(isEditing
      ? [
          {
            ...baseColumnConfig,
            field: 'orderSpecification',
            renderHeader: () => <div>PO2</div>,
            renderCell: (params) => {
              return params.row.playerOrder === 'PASS_TO_AREA' ||
                params.row.playerOrder === 'DRIBBLE_TO_AREA' ? (
                <Select
                  autoWidth
                  size="small"
                  value={params.row.playerOrderDestinationPitchArea}
                  onChange={(event) =>
                    handlePlayerOrderSpecChange(params.row, event.target.value)
                  }
                  sx={{
                    backgroundColor: 'white',
                    width: '200px',
                    '&:hover .MuiOutlinedInput-notchedOutline': {
                      borderColor: '#FF3F84',
                    },
                    '&.Mui-focused .MuiOutlinedInput-notchedOutline': {
                      borderColor: '#FF3F84',
                    },
                  }}
                  inputProps={{
                    style: { backgroundColor: 'white' },
                  }}>
                  {Object.values(PITCH_AREAS).map((menuValue) => (
                    <MenuItem key={String(menuValue)} value={String(menuValue)}>
                      {convertSnakeCaseToTitleCase(String(menuValue))}
                    </MenuItem>
                  ))}
                </Select>
              ) : null;
            },
          },
        ]
      : []),

    ...(!isEditing
      ? [
          {
            ...baseColumnConfig,
            field: 'status',
            minWidth: 50,
            renderHeader: () => <div>Status</div>,
            valueGetter: (params: GridValueGetterParams) =>
              convertSnakeCaseToTitleCase(params.row.status),
          },
        ]
      : []),
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
