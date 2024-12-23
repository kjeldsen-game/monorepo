import {
  GridCellParams,
  GridColDef,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
  PlayerPosition,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import MarketButton from '../../Market/MarketButton';
import { AuctionMarket } from '@/shared/models/Auction';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';

export const marketColumn = (
  handleButtonClick: (auction: AuctionMarket) => void,
) => {
  const filterCoreSkills = (actualSkills: Record<string, any>) => {
    return Object.entries(actualSkills)
      .filter(([, value]) => value.PlayerSkills.playerSkillRelevance === 'CORE')
      .map(([key]) => key);
  };

  const baseColumnConfig = {
    hideSortIcons: true,
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    flex: 1,
  };

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
        const actual =
          params.row.player.actualSkills[field]?.PlayerSkills.actual || 0;
        const potential =
          params.row.player.actualSkills[field]?.PlayerSkills.potential || 0;
        return `${actual}/${potential}`;
      },
    };
  };

  const columns: GridColDef[] = [
    {
      ...baseColumnConfig,
      field: 'name',
      renderHeader: () => <div>Name</div>,
      minWidth: 130,
      renderCell: (params: GridCellParams) => (
        <Link
          style={{ textDecoration: 'none', color: '#000000' }}
          passHref
          href={`/player/${params.row.player.id}`}>
          {params.row.player.name}
        </Link>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'age',
      renderHeader: () => <div>Age</div>,
      valueGetter: (params: GridValueGetterParams) =>
        params.row.player.age.years,
    },
    {
      ...baseColumnConfig,
      field: 'playerPosition',
      renderHeader: () => <div>Pos</div>,
      renderCell: (params) => {
        const position = params.row.player
          .position as keyof typeof PlayerPosition;
        const initials = getPositionInitials(position);
        return (
          <div
            style={{
              color: '#FFFFFF',
              padding: '2px 8px 2px 8px',
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
      minWidth: 50,
      flex: 1,
    },
    createSkillColumnConfig('SCORING', 'SC'),
    createSkillColumnConfig('OFFENSIVE_POSITIONING', 'OP'),
    createSkillColumnConfig('BALL_CONTROL', 'BC'),
    createSkillColumnConfig('PASSING', 'PA'),
    createSkillColumnConfig('TACKLING', 'TA'),
    createSkillColumnConfig('DEFENSIVE_POSITIONING', 'DP'),
    {
      ...baseColumnConfig,
      field: 'averageBid',
      renderHeader: () => <div>Average Bid</div>,
      minWidth: 70,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.averageBid} $</div>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'myBid',
      renderHeader: () => <div>My Bid</div>,
      minWidth: 70,
      renderCell: (params) => (
        <div style={{ fontWeight: 'bold' }}>{params.row.bid || 0} $</div>
      ),
    },
    {
      ...baseColumnConfig,
      field: 'totalBids',
      renderHeader: () => <div>Total bidders</div>,
      minWidth: 70,
      valueGetter: (params) => params.row.bidders,
    },
    {
      ...baseColumnConfig,
      field: 'action',
      renderHeader: () => <div>Action</div>,
      sortable: false,
      minWidth: 50,
      renderCell: (params: GridCellParams) => (
        <>
          <MarketButton
            sx={{ height: '34px', minWidth: '34px' }}
            children={'$'}
            onClick={() => handleButtonClick(params.row)}
          />
        </>
      ),
    },
  ];

  return columns;
};
