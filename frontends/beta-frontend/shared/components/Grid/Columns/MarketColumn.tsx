import {
    GridCellParams,
    GridColDef,
    GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import {
    PlayerPosition,
    PlayerPositionColor,
} from '@/shared/models/PlayerPosition';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import { Typography } from '@mui/material';
import { PlayerSkillToShortcut } from '@/shared/models/PlayerSkill';
import MarketButton from '../../Market/MarketButton';

export const marketColumn = (
    handleButtonClick: (auctionId: string) => void,
) => {
    const filterCoreSkills = (actualSkills: Record<string, any>) => {
        return Object.entries(actualSkills)
            .filter(
                ([, value]) =>
                    value.PlayerSkills.playerSkillRelevance === 'CORE',
            )
            .map(([key]) => key);
    };

    const columns: GridColDef[] = [
        {
            field: 'name',
            renderHeader: () => <div>Name</div>,
            headerAlign: 'center' as GridAlignment,
            minWidth: 130,
            flex: 1,
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
            field: 'age',
            renderHeader: () => <div>Age</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 70,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) =>
                params.row.player.age.years,
        },
        {
            field: 'playerPosition',
            renderHeader: () => <div>Pos</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            renderCell: (params) => {
                const position = params.row.player
                    .position as keyof typeof PlayerPosition;
                const initials = position
                    .split('_')
                    .map((word) => word.charAt(0).toUpperCase())
                    .join('');

                return (
                    <div
                        style={{
                            color: '#FFFFFF',
                            padding: '2px 8px 2px 8px',
                            width: '42px',
                            height: '24px',
                            borderRadius: '5px',
                            textAlign: 'center',
                            background: PlayerPositionColor[position],
                        }}>
                        {initials}
                    </div>
                );
            },
            minWidth: 50,
            flex: 1,
        },
        {
            field: 'Core Skills',
            renderHeader: () => <div>CS</div>,
            minWidth: 50,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            flex: 1,
            renderCell: (params) => {
                const coreSkills = filterCoreSkills(
                    params.row.player.actualSkills,
                );
                const [firstSkill, secondSkill] = coreSkills;

                return (
                    <div>
                        <span style={{ fontWeight: '900' }}>
                            {PlayerSkillToShortcut[firstSkill]}
                        </span>{' '}
                        /{PlayerSkillToShortcut[secondSkill]}
                    </div>
                );
            },
        },
        {
            field: 'TODO',
            renderHeader: () => <div>DV</div>,
            minWidth: 50,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            flex: 1,
            renderCell: (params) => (
                <div
                    style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                    }}>
                    17
                    <ArrowRightIcon sx={{ color: '#A4BC10' }} />
                    <Typography sx={{ fontWeight: '900' }}>18</Typography>
                </div>
            ),
        },
        {
            field: 'DEFENSIVE_POSITIONING',
            renderHeader: () => <div>DP</div>,
            minWidth: 50,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) => {
                const actual =
                    params.row.player.actualSkills.DEFENSIVE_POSITIONING
                        ?.PlayerSkills.actual || 0;
                const potential =
                    params.row.player.actualSkills.DEFENSIVE_POSITIONING
                        ?.PlayerSkills.potential || 0;
                return `${actual}/${potential}`;
            },
        },
        {
            field: 'BALL_CONTROL',
            renderHeader: () => <div>BC</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 50,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) => {
                const actual =
                    params.row.player.actualSkills.BALL_CONTROL?.PlayerSkills
                        .actual || 0;
                const potential =
                    params.row.player.actualSkills.BALL_CONTROL?.PlayerSkills
                        .potential || 0;
                return `${actual}/${potential}`;
            },
        },
        {
            field: 'SCORE',
            renderHeader: () => <div>SC</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 50,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) => {
                const actual =
                    params.row.player.actualSkills.SCORING?.PlayerSkills
                        .actual || 0;
                const potential =
                    params.row.player.actualSkills.SCORING?.PlayerSkills
                        .potential || 0;
                return `${actual}/${potential}`;
            },
        },
        {
            field: 'PASSING',
            renderHeader: () => <div>PA</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 50,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) => {
                const actual =
                    params.row.player.actualSkills.PASSING?.PlayerSkills
                        .actual || 0;
                const potential =
                    params.row.player.actualSkills.PASSING?.PlayerSkills
                        .potential || 0;
                return `${actual}/${potential}`;
            },
        },
        {
            field: 'OFFENSIVE_POSITIONING',
            renderHeader: () => <div>OP</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 50,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) => {
                const actual =
                    params.row.player.actualSkills.OFFENSIVE_POSITIONING
                        ?.PlayerSkills.actual || 0;
                const potential =
                    params.row.player.actualSkills.OFFENSIVE_POSITIONING
                        ?.PlayerSkills.potential || 0;
                return `${actual}/${potential}`;
            },
        },
        {
            field: 'TACKLING',
            renderHeader: () => <div>TA</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 50,
            flex: 1,
            valueGetter: (params: GridValueGetterParams) => {
                const actual =
                    params.row.player.actualSkills.TACKLING?.PlayerSkills
                        .actual || 0;
                const potential =
                    params.row.player.actualSkills.TACKLING?.PlayerSkills
                        .potential || 0;
                return `${actual}/${potential}`;
            },
        },
        {
            field: 'averageBid',
            renderHeader: () => <div>Average Bid</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 70,
            flex: 1,
            renderCell: (params) => (
                <div style={{ fontWeight: 'bold' }}>
                    {params.row.averageBid} $
                </div>
            ),
        },
        {
            field: 'totalBids',
            renderHeader: () => <div>Total bids</div>,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 70,
            flex: 1,
            valueGetter: (params) => params.row.bids,
        },
        {
            field: 'action',
            renderHeader: () => <div>Action</div>,
            sortable: false,
            headerAlign: 'center' as GridAlignment,
            align: 'center' as GridAlignment,
            minWidth: 50,
            flex: 1,
            renderCell: (params: GridCellParams) => (
                <>
                    <MarketButton
                        sx={{ height: '34px', minWidth: '34px' }}
                        children={'$'}
                        onClick={() => handleButtonClick(params.row.auctionId)}
                    />
                </>
            ),
        },
    ];

    return columns;
};
