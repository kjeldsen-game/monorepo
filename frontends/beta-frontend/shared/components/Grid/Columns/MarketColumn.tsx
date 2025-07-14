import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import ColHeader from './common/components/ColHeader';
import { AuctionMarket } from '@/shared/models/market/Auction';
import { getColumnConfig } from './common/config/ColumnsConfig';
import { PlayerNameColumn } from './common/columns/PlayerNameColumn';
import { PlayerPositionColumn } from './common/columns/PlayerPositionColumn';
import { PlayerSkillColumn } from './common/columns/PlayerSkillColumn';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';
import CustomButton from '../../Common/CustomButton';
import { PlayerAgeColumn } from './common/columns/PlayerAgeColumn';
import { Box } from '@mui/material';

export const marketColumn = (
  handleButtonClick: (auction: AuctionMarket) => void,
  isXs: boolean
) => {
  const columns: GridColDef[] = [
    PlayerNameColumn((row) => row.player),
    PlayerAgeColumn((row) => row.player),
    PlayerPositionColumn(
      (row) => row.player,
      'preferredPosition',
      'Pos'
    ),
    PlayerSkillColumn((row) => row.player, PlayerSkill.SCORING, PlayerSkill.REFLEXES, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkill.GOALKEEPER_POSITIONING, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.BALL_CONTROL, PlayerSkill.INTERCEPTIONS, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.PASSING, PlayerSkill.CONTROL, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.AERIAL, PlayerSkill.ORGANIZATION, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.CONSTITUTION, PlayerSkill.ONE_ON_ONE, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.TACKLING, undefined, isXs),
    PlayerSkillColumn((row) => row.player, PlayerSkill.DEFENSIVE_POSITIONING, undefined, isXs),
    {
      ...getColumnConfig(),
      field: 'averageBid',
      renderHeader: () => <ColHeader header={'Average Bid'} />,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.averageBid} $</div>
      ),
    },
    {
      ...getColumnConfig(),
      field: 'myBid',
      renderHeader: () => <ColHeader header={'My Bid'} />,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.bid || 0} $</div>
      ),
    },
    {
      ...getColumnConfig(),
      field: 'totalBids',
      renderHeader: () => <ColHeader header={'Total Bidders'} />,
      renderCell: (params: GridCellParams) => (
        <div>{params.row.bidders}</div>
      )
    },
    {
      ...getColumnConfig("right"),
      field: "action",
      renderHeader: () => <ColHeader header={"Action"} align={'right'} />,
      renderCell: (params: GridCellParams) => (
        <Box height={'100%'} width={'100%'} display={'flex'} justifyContent={'end'} alignItems={'center'}>
          <CustomButton
            variant={'outlined'}
            sx={{ height: '34px', minWidth: '34px', marginRight: '10px' }}
            onClick={() => handleButtonClick(params.row)}>
            $
          </CustomButton>
        </Box>

      )
    }
  ];

  return columns;
};
