import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { baseColumnConfig } from './ColumnsConfig';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { playerCommonColumns } from './PlayerCommonColumns';
import MarketButton from '../../Market/MarketButton';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import { Player } from '@/shared/models/Player';
import { PITCH_AREAS } from '@/shared/models/PitchArea';
import { MenuItem, Select } from '@mui/material';
import { PlayerOrder } from '@/shared/models/PlayerOrder';
import SelectInput from '../../Common/SelectInput';

export const lineupColumn = (
  isEditing: boolean,
  handleActionButtonClick: (player: any) => void,
  handlePlayerChange?: (
    value: Player,
    order: PlayerOrder | any,
    field: any,
  ) => void,
) => {
  const handlePlayerOrderChange = (
    player: Player,
    value: PlayerOrder,
  ): void => {
    handlePlayerChange?.(player, value, 'playerOrder');
  };

  const handlePlayerOrderSpecChange = (player: Player, value: any): void => {
    handlePlayerChange?.(player, value, 'playerOrderDestinationPitchArea');
  };

  const columns: GridColDef[] = [
    ...playerCommonColumns(true, false),
    ...(isEditing
      ? [
          {
            ...baseColumnConfig,
            field: 'playerOrder',
            renderHeader: () => <div>PO</div>,
            renderCell: (params) => (
              <SelectInput
                value={params.row.playerOrder || ''}
                values={PlayerOrder}
                sx={{ width: '80px' }}
                handleChange={(event) =>
                  handlePlayerOrderChange(params.row, event.target.value)
                }
              />
            ),
          },
        ]
      : [
          {
            ...baseColumnConfig,
            field: 'playerOrder',
            renderHeader: () => <div>PO</div>,
            renderCell: (params) => (
              <div>{convertSnakeCaseToTitleCase(params.row.playerOrder)}</div>
            ),
          },
        ]),

    ...(isEditing
      ? [
          {
            ...baseColumnConfig,
            field: 'orderSpecification',
            renderHeader: () => <div>PO2</div>,
            renderCell: (params) =>
              params.row.playerOrder === 'PASS_TO_AREA' ||
              params.row.playerOrder === 'DRIBBLE_TO_AREA' ? (
                <SelectInput
                  value={params.row.playerOrderDestinationPitchArea || ''}
                  values={PITCH_AREAS}
                  sx={{ width: '80px' }}
                  handleChange={(event) =>
                    handlePlayerOrderSpecChange(params.row, event.target.value)
                  }
                />
              ) : null,
          },
        ]
      : [
          {
            ...baseColumnConfig,
            field: 'orderSpecification',
            renderHeader: () => <div>PO2</div>,
            renderCell: (params) => (
              <div>
                {convertSnakeCaseToTitleCase(
                  params.row.playerOrderDestinationPitchArea,
                )}
              </div>
            ),
          },
        ]),

    {
      ...baseColumnConfig,
      field: 'action',
      renderHeader: () => <div>Action</div>,
      renderCell: (params: GridCellParams) => (
        <MarketButton
          sx={{ height: '34px', minWidth: '34px', paddingX: '12px' }}
          onClick={() => handleActionButtonClick(params.row)}>
          <PeopleAltIcon />
        </MarketButton>
      ),
    },
  ];

  return columns;
};
