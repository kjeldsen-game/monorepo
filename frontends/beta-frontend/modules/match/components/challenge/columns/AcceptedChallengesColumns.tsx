import { GridCellParams, GridColDef } from '@mui/x-data-grid';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import { Box } from '@mui/material';
import { TeamNameColumn } from '../../columns/TeamNameColumn';
import LinkButton from '@/shared/components/Common/LinkButton';
import CustomIconButton from '@/shared/components/Common/CustomIconButton';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import { ClearIcon } from '@mui/x-date-pickers';

const AcceptedChallengesColumns = (
    teamId: string,
    handlePlayButtonClick: (matchId: string) => void,
    handleCancelButtonBlick: (matchId: string) => void,
): GridColDef[] => [
        TeamNameColumn((row) => row.home, "left", "Home"),
        TeamNameColumn((row) => row.away, "center", "Away"),
        {
            field: 'lineup',
            minWidth: 20,
            renderHeader: () => <ColHeader header="Lineup" />,
            ...getColumnConfig(),
            renderCell: (params: GridCellParams) => {
                const team =
                    params.row.away.id === teamId
                        ? params.row.away
                        : params.row.home.id === teamId
                            ? params.row.home
                            : null;
                return team ? (
                    team.specificLineup ? (
                        <div>Custom</div>
                    ) : (
                        <div>Default</div>
                    )
                ) : null;
            },
        },
        {
            field: 'actions',
            renderHeader: () => <ColHeader header="Action" align={'right'} />,
            ...getColumnConfig('right'),
            minWidth: 180,
            renderCell: (params: GridCellParams) => {
                return (
                    <Box sx={{ marginRight: '10px', height: '100%' }} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                        {params.row.status === 'ACCEPTED' &&
                            params.row.home.id === teamId && (
                                <CustomIconButton onClick={() => handlePlayButtonClick(params.row.id)} sx={{ marginRight: '4px' }}>
                                    <PlayArrowIcon />
                                </CustomIconButton>
                            )}
                        <Box sx={{ height: '100%' }} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                            {params.row.away.name === params.row.home.name ? (
                                // Self challenge
                                <Box sx={{ height: '100%' }} display={'flex'} justifyContent={'end'} alignItems={'center'}>
                                    <CustomIconButton onClick={() => handleCancelButtonBlick(params.row.id)} sx={{ marginRight: '4px' }}>
                                        <ClearIcon />
                                    </CustomIconButton>
                                    <LinkButton variant='contained' link={`/match/self/${params.row.id}`}>
                                        Set Lineup
                                    </LinkButton>
                                </Box>
                            ) : (
                                <LinkButton variant='contained' link={`/match/lineup/${params.row.id}`}>
                                    Set Lineup
                                </LinkButton>
                            )}
                        </Box>
                    </Box>
                );
            },
        },
    ];

export default AcceptedChallengesColumns;
