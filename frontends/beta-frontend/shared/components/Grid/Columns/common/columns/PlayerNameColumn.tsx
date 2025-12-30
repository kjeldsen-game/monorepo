import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Player } from '@/shared/models/player/Player';
import ColHeader from '../components/ColHeader';
import ColLink from '../components/ColLink';
import { getColumnConfig } from '../config/ColumnsConfig';
import { formatName, getSurname } from '@/shared/utils/PlayerUtils';
import { theme } from '@/libs/material/theme';
import { Box } from '@mui/material';

export const PlayerNameColumn = (
    getPlayerValue: (row: any) => Player,
    alignment: GridAlignment = 'left',
    getIcon?: (row: any) => React.ElementType | null // returns component type
): GridColDef => {
    return {
        ...getColumnConfig(alignment),
        field: 'name',
        renderHeader: () => <ColHeader header="Name" align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const paddingStyle =
                alignment === 'left'
                    ? { paddingLeft: '10px' }
                    : alignment === 'right'
                        ? { paddingRight: '10px' }
                        : {};
            const player: Player = getPlayerValue(params.row);
            const IconComponent = getIcon?.(params.row); // get component type
            return (
                <Box display={'flex'}>


                    <ColLink sx={{ ...paddingStyle, fontWeight: 'bold' }} urlValue={`/player/${player.id}`}>
                        <Box display={'flex'} alignItems={'center'} justifyContent={'center'} >
                            {IconComponent && <IconComponent sx={{ fontSize: 20, color: theme.palette.secondary.main, mr: 1 }} />}
                            {formatName(player.name)}
                        </Box>
                    </ColLink>
                </Box>
            );
        },
    };
};
