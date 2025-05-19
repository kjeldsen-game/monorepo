import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import { baseColumnConfig, rightColumnConfig } from './ColumnsConfig';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { playerCommonColumns } from './PlayerCommonColumns';
import MarketButton from '../../Market/MarketButton';
import PeopleAltIcon from '@mui/icons-material/PeopleAlt';
import { Player } from '@/shared/models/player/Player';
import { PITCH_AREAS } from '@/shared/models/match/PitchArea';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import SelectInput from '../../Common/SelectInput';
import ColHeader from './Common/ColHeader';
import CloseIcon from '@mui/icons-material/Close';

export const lineupColumn = (
  isEditing: boolean,
  handleActionButtonClick: (player: any) => void,
  activePlayer: Player | undefined,
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
    ...playerCommonColumns(true, true, true),
    ...(isEditing
      ? [
          {
            ...baseColumnConfig,
            field: 'playerOrder',
            renderHeader: () => <ColHeader header={'PO'} />,
            renderCell: (params: GridCellParams) => (
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
            renderHeader: () => <ColHeader header={'PO'} />,
            renderCell: (params: GridCellParams) => (
              <div>{convertSnakeCaseToTitleCase(params.row.playerOrder)}</div>
            ),
          },
        ]),
    ...(isEditing
      ? [
          {
            ...baseColumnConfig,
            field: 'orderSpecification',
            renderHeader: () => <ColHeader header={'PO2'} />,
            renderCell: (params: GridCellParams) =>
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
            renderHeader: () => <ColHeader header={'PO2'} />,
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
      ...rightColumnConfig,
      field: 'action',
      renderHeader: () => <ColHeader header={'Action'} align={'right'} />,
      renderCell: (params: GridCellParams) => (
        <MarketButton
          disabled={activePlayer && activePlayer?.id !== params.row.id}
          sx={{
            height: '34px',
            minWidth: '34px',
            paddingX: '12px',
            marginRight: '10px',
          }}
          onClick={() => {
            handleActionButtonClick(params.row);
          }}>
          {activePlayer?.id === params.row.id ? (
            <CloseIcon />
          ) : (
            <PeopleAltIcon />
          )}
        </MarketButton>
      ),
    },
  ];

  return columns;
};
