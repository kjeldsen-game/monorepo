import {
  GridCellParams,
  GridColDef,
  GridComparatorFn,
  GridValueGetterParams,
} from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import Link from 'next/link';
import { PlayerOrderSelect } from '../../PlayerOrderSelect';
import { PlayerPositionSelect } from '../../PlayerPositionSelect';
import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { Player } from '@/shared/models/Player';
import { PlayerOrder } from '@/shared/models/PlayerOrder';
import { PlayerStatusSelect } from '../../PlayerStatusSelect';
import { PlayerLineupStatus } from '@/shared/models/PlayerLineupStatus';
import { baseColumnConfig } from './ColumnsConfig';

export const teamColumn = (
  isEditing: boolean,
  handlePlayerChange?: (value: Player) => void,
) => {
  const statusComparator: GridComparatorFn<string> = (v1, v2) => {
    const order = ['ACTIVE', 'INACTIVE', 'BENCH', 'FOR_SALE'];
    const index1 = order.indexOf(v1);
    const index2 = order.indexOf(v2);
    const result = index1 - index2;
    return result;
  };

  const createSkillColumnConfig = (
    field: string,
    headerName: string,
    fieldSecondary?: string,
    headerNameSecondary?: string,
  ): GridColDef => {
    return {
      ...baseColumnConfig,
      field,
      renderHeader: () => (
        <div>
          {headerName}
          {fieldSecondary}
        </div>
      ),
      minWidth: 50,
      valueGetter: (params: GridValueGetterParams) => {
        const actual =
          fieldSecondary &&
          headerNameSecondary &&
          params.row.actualSkills[fieldSecondary]?.PlayerSkills?.actual !== 0
            ? params.row.actualSkills[fieldSecondary]?.PlayerSkills?.actual
            : params.row.actualSkills[field]?.PlayerSkills?.actual;

        const potential =
          fieldSecondary &&
          headerNameSecondary &&
          params.row.actualSkills[fieldSecondary]?.PlayerSkills?.potential !== 0
            ? params.row.actualSkills[fieldSecondary]?.PlayerSkills?.potential
            : params.row.actualSkills[field]?.PlayerSkills?.potential;

        return `${actual}/${potential}`;
      },
    };
  };

  const handlePlayerPositionChange = (
    player: Player,
    value: PlayerPosition,
  ): void => {
    handlePlayerChange?.({ ...player, position: value });
  };

  const handlePlayerOrderChange = (
    player: Player,
    value: PlayerOrder,
  ): void => {
    handlePlayerChange?.({ ...player, playerOrder: value });
  };

  const handlePlayerStatusChange = (
    player: Player,
    value: PlayerLineupStatus,
  ): void => {
    handlePlayerChange?.({ ...player, status: value });
  };

  const columns: GridColDef[] = [
    // { field: 'status', headerName: 'Health', headerAlign: 'center' as GridAlignment, align: 'center' as GridAlignment, minWidth: 70, flex: 1 },
    {
      field: 'name',
      headerName: 'Name',
      headerAlign: 'center' as GridAlignment,
      minWidth: 130,
      flex: 1,
      renderCell: (params: GridCellParams) => (
        <Link passHref href={`/player/${params.row.id}`}>
          {params.row.name}
        </Link>
      ),
    },
    {
      field: 'age',
      headerName: 'Age',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      minWidth: 70,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => params.row.age.years,
    },
    createSkillColumnConfig('SCORING', 'SC', 'REFLEXES', 'RE'),
    createSkillColumnConfig(
      'OFFENSIVE_POSITIONING',
      'OP',
      'GOALKEEPER_POSITIONING',
      'GP',
    ),
    createSkillColumnConfig('BALL_CONTROL', 'BC', 'INTERCEPTIONS', 'IN'),
    createSkillColumnConfig('PASSING', 'PA', 'CONTROL', 'CT'),
    createSkillColumnConfig('AERIAL', 'AE', 'ORGANIZATION', 'OR'),
    createSkillColumnConfig('CONSTITUTION', 'CO'),
    createSkillColumnConfig('TACKLING', 'TA'),
    createSkillColumnConfig('DEFENSIVE_POSITIONING', 'DP'),
    // {
    //   field: 'DEFENSIVE_POSITIONING',
    //   renderHeader: () => (
    //     <div>
    //       DP<sup>CO</sup>
    //     </div>
    //   ),
    //   minWidth: 50,
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => {
    //     return (
    //       params.row.actualSkills.DEFENSIVE_POSITIONING?.PlayerSkills.actual ||
    //       params.row.actualSkills.CONTROL?.PlayerSkills.actual
    //     );
    //   },
    // },
    // {
    //   field: 'BALL_CONTROL',
    //   renderHeader: () => (
    //     <div>
    //       BC<sup>INT</sup>
    //     </div>
    //   ),
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => {
    //     return (
    //       params.row.actualSkills.BALL_CONTROL?.PlayerSkills.actual ||
    //       params.row.actualSkills.INTERCEPTIONS?.PlayerSkills.actual
    //     );
    //   },
    // },
    // {
    //   field: 'SCORE',
    //   renderHeader: () => (
    //     <div>
    //       SC<sup>1on1</sup>
    //     </div>
    //   ),
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => {
    //     return (
    //       params.row.actualSkills.SCORING?.PlayerSkills.actual ||
    //       params.row.actualSkills.ONE_ON_ONE?.PlayerSkills.actual
    //     );
    //   },
    // },
    // {
    //   field: 'PASSING',
    //   renderHeader: () => (
    //     <div>
    //       PA<sup>ORG</sup>
    //     </div>
    //   ),
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => {
    //     return (
    //       params.row.actualSkills.PASSING?.PlayerSkills.actual ||
    //       params.row.actualSkills.ORGANIZATION?.PlayerSkills.actual
    //     );
    //   },
    // },
    // {
    //   field: 'OFFENSIVE_POSITIONING',
    //   renderHeader: () => (
    //     <div>
    //       OP<sup>POS</sup>
    //     </div>
    //   ),
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => {
    //     return (
    //       params.row.actualSkills.OFFENSIVE_POSITIONING?.PlayerSkills.actual ||
    //       params.row.actualSkills.POSITIONING?.PlayerSkills.actual
    //     );
    //   },
    // },
    // {
    //   field: 'TACKLING',
    //   renderHeader: () => (
    //     <div>
    //       TA<sup>RE</sup>
    //     </div>
    //   ),
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => {
    //     return (
    //       params.row.actualSkills.TACKLING?.PlayerSkills.actual ||
    //       params.row.actualSkills.REFLEXES?.PlayerSkills.actual
    //     );
    //   },
    // },
    // {
    //   field: 'CO',
    //   headerName: 'CO',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO?.PlayerSkills.actual,
    // },
    {
      field: 'playerPosition',
      headerName: 'PP',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      renderCell: (params) => {
        return isEditing ? (
          <PlayerPositionSelect
            onChange={(value) => handlePlayerPositionChange(params.row, value)}
            value={
              PlayerPosition[
                params.row.position as keyof typeof PlayerPosition
              ] ?? undefined
            }
          />
        ) : undefined;
      },
      // valueGetter: (params) =>
      //   PlayerPosition[params.row.position as keyof typeof PlayerPosition],
      minWidth: 50,
      flex: 1,
      editable: isEditing,
    },
    {
      field: 'playerOrder',
      headerName: 'PO',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      renderCell: (params) => {
        return isEditing ? (
          <PlayerOrderSelect
            onChange={(value) => handlePlayerOrderChange(params.row, value)}
            value={
              PlayerOrder[params.row.playerOrder as keyof typeof PlayerOrder] ??
              undefined
            }
          />
        ) : undefined;
      },
      valueGetter: (params) =>
        PlayerOrder[params.row.playerOrder as keyof typeof PlayerOrder],
      minWidth: 150,
      flex: 1,
      editable: isEditing,
    },
    {
      field: 'playerStatus',
      headerName: 'Status',
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      valueGetter: (params: GridValueGetterParams) => params.row.status,
      renderCell: (params) => (
        <PlayerStatusSelect
          value={params.row.status}
          onChange={(value) =>
            handlePlayerStatusChange(params.row as Player, value)
          }
        />
      ),
      minWidth: 20,
      flex: 1,
      editable: isEditing,
      sortComparator: statusComparator,
    },
    // {
    //   field: 'GP',
    //   headerName: 'GP',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'GOALS',
    //   headerName: 'GLs',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'ASSISTS',
    //   headerName: 'As',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'TAKEDOWNS',
    //   headerName: 'Ta',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'CRD',
    //   headerName: 'Crd',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'MOM',
    //   headerName: 'MoM',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'RATING',
    //   headerName: 'Rating',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   minWidth: 50,
    //   flex: 1,
    //   valueGetter: (params: GridValueGetterParams) => params.row.actualSkills.CO,
    // },
    // {
    //   field: 'playerOrder1',
    //   headerName: 'PO1',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder2',
    //   headerName: 'PO2',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder3',
    //   headerName: 'PO3',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder4',
    //   headerName: 'PO4',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
    // {
    //   field: 'playerOrder5',
    //   headerName: 'PO5',
    //   headerAlign: 'center' as GridAlignment,
    //   align: 'center' as GridAlignment,
    //   renderCell: (params: GridCellParams) => <PlayerOrderSelect />,
    //   minWidth: 70,
    //   flex: 1,
    // },
  ];

  return columns;
};
