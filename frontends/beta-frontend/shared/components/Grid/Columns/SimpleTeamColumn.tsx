import {
  PlayerPosition,
  PlayerPositionColor,
  PlayerPositionColorNew,
} from '@/shared/models/PlayerPosition';
import { formatName } from '@/shared/utils/MatchReportUtils';
import { GridColDef, GridValueGetterParams } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import { TFunction } from 'i18next';

export const simpleTeamColumn = (t: TFunction) => {
  const columns: GridColDef[] = [
    {
      field: 'playerPosition',
      renderHeader: () => <div>POS</div>,
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      renderCell: (params) => {
        const position = params.row.position as keyof typeof PlayerPosition;
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
              background: PlayerPositionColorNew[position],
            }}>
            {initials}
          </div>
        );
      },
      // minWidth: 20,
      // maxWidth: 50,
      flex: 1,
    },
    {
      field: 'playerName',
      renderHeader: () => (
        <div>
          Name
          {/* <sup>CO</sup> */}
        </div>
      ),
      minWidth: 20,
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'left' as GridAlignment,
      flex: 1,

      renderCell: (params) => {
        return (
          <div style={{ padding: '5px' }}>{formatName(params.row.name)}</div>
        );
      },
    },
    {
      field: 'DEFENSIVE_POSITIONING',
      renderHeader: () => (
        <div>
          DP
          {/* <sup>CO</sup> */}
        </div>
      ),
      maxWidth: 30,
      minWidth: 20,
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      flex: 1,

      renderCell: (params) => {
        return (
          <div>
            {params.row.actualSkills.DEFENSIVE_POSITIONING?.PlayerSkills
              .actual || params.row.actualSkills.CONTROL?.PlayerSkills.actual}
          </div>
        );
      },
    },
    {
      field: 'BALL_CONTROL',
      renderHeader: () => (
        <div>
          BC
          {/* <sup>INT</sup> */}
        </div>
      ),
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      maxWidth: 30,
      minWidth: 20,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return (
          params.row.actualSkills.BALL_CONTROL?.PlayerSkills.actual ||
          params.row.actualSkills.INTERCEPTIONS?.PlayerSkills.actual
        );
      },
    },
    {
      field: 'SCORE',
      renderHeader: () => (
        <div>
          SC
          {/* <sup>1on1</sup> */}
        </div>
      ),
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      maxWidth: 30,
      minWidth: 20,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return (
          params.row.actualSkills.SCORING?.PlayerSkills.actual ??
          params.row.actualSkills.ONE_ON_ONE?.PlayerSkills.actual
        );
      },
    },
    {
      field: 'PASSING',
      renderHeader: () => (
        <div>
          PA
          {/* <sup>ORG</sup> */}
        </div>
      ),
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      maxWidth: 30,
      minWidth: 20,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return (
          params.row.actualSkills.PASSING?.PlayerSkills.actual ||
          params.row.actualSkills.ORGANIZATION?.PlayerSkills.actual
        );
      },
    },
    {
      field: 'OFFENSIVE_POSITIONING',
      renderHeader: () => (
        <div>
          OP
          {/* <sup>POS</sup> */}
        </div>
      ),
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      maxWidth: 30,
      minWidth: 20,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return (
          params.row.actualSkills.OFFENSIVE_POSITIONING?.PlayerSkills.actual ||
          params.row.actualSkills.GOALKEEPER_POSITIONING?.PlayerSkills.actual
        );
      },
    },
    {
      field: 'TACKLING',
      renderHeader: () => (
        <div>
          TA
          {/* <sup>RE</sup> */}
        </div>
      ),
      disableColumnMenu: true,
      hideSortIcons: true,
      headerAlign: 'center' as GridAlignment,
      align: 'center' as GridAlignment,
      maxWidth: 30,
      minWidth: 20,
      flex: 1,
      valueGetter: (params: GridValueGetterParams) => {
        return (
          params.row.actualSkills.TACKLING?.PlayerSkills.actual ||
          params.row.actualSkills.REFLEXES?.PlayerSkills.actual
        );
      },
    },
  ];

  return columns;
};
