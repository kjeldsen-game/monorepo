import { GridColDef } from '@mui/x-data-grid';
import { GridAlignment } from '@mui/x-data-grid';
import ProgressBar from '../../../Training/ProgressBar';
import ArrowRightIcon from '@mui/icons-material/ArrowRight';
import { Box, Typography } from '@mui/material';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { playerCommonColumns } from '../PlayerCommonColumns';
import ColHeader from '../Common/ColHeader';
import { rightColumnConfig } from '../ColumnsConfig';

export const trainingColumn = () => {
  const baseColumnConfig = {
    hideSortIcons: true,
    headerAlign: 'center' as GridAlignment,
    align: 'center' as GridAlignment,
    flex: 1,
  };

  const columns: GridColDef[] = [
    ...playerCommonColumns(false, false, true),
    // {
    //   ...baseColumnConfig,
    //   field: 'name',
    //   renderHeader: () => <div>Name</div>,
    //   minWidth: 130,
    //   renderCell: (params: GridCellParams) => (
    //     <Link
    //       style={{ textDecoration: 'none', color: '#000000' }}
    //       passHref
    //       href={`/player/${params.row.player.id}`}>
    //       {params.row.player.name}
    //     </Link>
    //   ),
    // },
    // {
    //   ...baseColumnConfig,
    //   field: 'age',
    //   renderHeader: () => <div>Age</div>,
    //   minWidth: 70,
    //   valueGetter: (params: GridValueGetterParams) =>
    //     params.row.player.age.years,
    // },
    // {
    //   ...baseColumnConfig,
    //   field: 'playerPosition',
    //   renderHeader: () => <div>Position</div>,
    //   renderCell: (params) => {
    //     const position = params.row.player
    //       .position as keyof typeof PlayerPosition;
    //     const initials = getPositionInitials(position);

    //     return (
    //       <div
    //         style={{
    //           color: '#FFFFFF',
    //           padding: '2px 8px 2px 8px',
    //           width: '42px',
    //           height: '24px',
    //           borderRadius: '5px',
    //           textAlign: 'center',
    //           background: PlayerPositionColorNew[position],
    //         }}>
    //         {initials}
    //       </div>
    //     );
    //   },
    //   minWidth: 50,
    //   flex: 1,
    // },
    {
      ...baseColumnConfig,
      field: 'cv',
      renderHeader: () => <ColHeader header={'Skill'} />,
      minWidth: 70,
      renderCell: (params) => {
        return <Box>{convertSnakeCaseToTitleCase(params.row.skill)}</Box>;
      },
    },
    {
      ...baseColumnConfig,
      field: 'dv',
      renderHeader: () => <ColHeader header={'DV'} />,
      minWidth: 70,
      renderCell: (params) => {
        return (
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            {params.row.pointsBeforeTraining}
            <ArrowRightIcon sx={{ color: '#A4BC10' }} />
            <Typography sx={{ fontWeight: 'bold' }}>
              {' '}
              {params.row.pointsAfterTraining}
            </Typography>
          </Box>
        );
      },
    },
    {
      ...rightColumnConfig,
      field: 'day',
      renderHeader: () => <ColHeader header={'PB'} />,
      minWidth: 70,
      renderCell: (params) => {
        return (
          <div>
            <ProgressBar days={params.row.currentDay} />
          </div>
        );
      },
    },
  ];

  return columns;
};
