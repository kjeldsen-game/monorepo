import { GridCellParams, GridColDef } from '@mui/x-data-grid';

import { Box } from '@mui/material';
import { PlayerNameColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerNameColumn';
import { PlayerAgeColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerAgeColumn';
import { PlayerPositionColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerPositionColumn';
import { PlayerSkillColumn } from '@/shared/components/Grid/Columns/common/columns/PlayerSkillColumn';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import CustomButton from '@/shared/components/Common/CustomButton';
import { PriceColumn } from './common/PriceColumn';
import { AuctionResponse } from 'modules/market/types/responses';
import Countdown from '../Countdown';

export const MarketColumns = (
    isXs: boolean,
    showOnlyPlayer?: boolean,
    handleButtonClick?: (auction: AuctionResponse) => void,
) => {

    const playerColumns: GridColDef[] = [
        PlayerNameColumn((row) => row.player, 'left'),
        PlayerAgeColumn((row) => row.player),
        PlayerPositionColumn((row) => row.player, 'preferredPosition', 'Pos'),

        PlayerSkillColumn((row) => row.player, PlayerSkill.SCORING, PlayerSkill.REFLEXES, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.OFFENSIVE_POSITIONING, PlayerSkill.GOALKEEPER_POSITIONING, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.BALL_CONTROL, PlayerSkill.INTERCEPTIONS, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.PASSING, PlayerSkill.CONTROL, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.AERIAL, PlayerSkill.ORGANIZATION, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.CONSTITUTION, PlayerSkill.ONE_ON_ONE, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.TACKLING, undefined, isXs),
        PlayerSkillColumn((row) => row.player, PlayerSkill.DEFENSIVE_POSITIONING, undefined, isXs),
    ];
    if (showOnlyPlayer) {
        return playerColumns;
    }
    const columns: GridColDef[] = [
        ...playerColumns,

        PriceColumn((row) => row.averageBid, "center", "Average Bid"),
        PriceColumn((row) => row.bid, "center", "My Bid"),

        {
            ...getColumnConfig(),
            field: 'totalBids',
            renderHeader: () => <ColHeader header={'Total Bidders'} />,
            renderCell: (params: GridCellParams) => (
                <div>{params.row.bidders}</div>
            )
        },
        {
            ...getColumnConfig("center"),
            field: "countdown",
            renderHeader: () => <ColHeader header={"Ends In"} />,
            renderCell: (params: GridCellParams) => (
                <Countdown endedAt={params.row.endedAt} />
            ),
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
                        onClick={() => handleButtonClick(params.row)}
                    >
                        $
                    </CustomButton>
                </Box>
            )
        }
    ];

    return columns;
};
