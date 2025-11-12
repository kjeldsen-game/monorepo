import { GridAlignment, GridCellParams, GridColDef } from '@mui/x-data-grid';
import { Player } from '@/shared/models/player/Player';
import { getColumnConfig } from '@/shared/components/Grid/Columns/common/config/ColumnsConfig';
import ColHeader from '@/shared/components/Grid/Columns/common/components/ColHeader';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import { Box } from '@mui/material';
import { Rating } from 'modules/player/types/Player';

export const RatingColumn = (
    getRatingValue: (row: any) => Rating,
    alignment: GridAlignment = 'center',
    header: string,
): GridColDef => {
    return {
        ...getColumnConfig(),
        field: header,
        renderHeader: () => <ColHeader header={header} align={alignment} />,
        renderCell: (params: GridCellParams) => {
            const rating: Rating = getRatingValue(params.row);
            return (
                <Box display={'flex'} justifyContent={'center'} alignItems={'center'} height={'100%'}>
                    {rating.actual}/{rating.potential}
                </Box>
            );
        },
    };
};
