import { DataGrid, DataGridProps, GridColDef } from '@mui/x-data-grid';
import Box from '@mui/material/Box';

type GridProps<T> = {
  rows: T[];
  columns: GridColDef[];
} & DataGridProps;

function Grid<T>({ rows, columns, ...props }: GridProps<T>) {
  return (
    <Box
      sx={{
        overflow: 'hidden',
        // '& .MuiDataGrid-scrollbarFiller': {
        //   display: 'none',
        // },
        // '& .MuiDataGrid-filler': {
        //   display: 'none'
        // },
        // '& .MuiDataGrid-scrollbarFiller--header': {
        //   display: 'none',
        // },
        width: '100%',
        '& .super-app .MuiDataGrid-cellContent': {
          color: 'white',
          padding: '6px',
          fontWeight: '600',
        },
        '& .MuiDataGrid-columnHeader': {
          padding: '0px !important',
        },
        '& .MuiDataGrid-columnSeparator': {
          display: 'none',
        },
        '& .MuiDataGrid-columnHeaders': {
          fontWeight: 900,
          padding: '0px !important',
        },
        '& .MuiDataGrid-cell': {
          padding: '0px !important',
        },
        '& .MuiDataGrid-cell:focus-within, .MuiDataGrid-cell:focus': {
          outline: 'none !important',
        },
        '& .super-app.goalkeeper .MuiDataGrid-cellContent': {
          backgroundColor: '#A4BC10',
        },
        '& .super-app.defender .MuiDataGrid-cellContent': {
          backgroundColor: '#F68B29',
        },
        '& .super-app.middle .MuiDataGrid-cellContent': {
          backgroundColor: '#29B6F6',
        },
        '& .super-app.forward .MuiDataGrid-cellContent': {
          backgroundColor: '#E52323',
        },
        '.MuiDataGrid-iconButtonContainer': {
          visibility: 'visible',
        },
        '.MuiDataGrid-sortIcon': {
          opacity: 'inherit !important',
        },
        '&.MuiDataGrid-root': {
          outline: 'none !important',
        },
      }}>
      <DataGrid
        // style={{
        //   '--DataGrid-rowWidth': '800px',
        // }}
        sx={{
          '& .MuiDataGrid-row': {
            opacity: 0,
            transform: 'translateY(10px)',
            animation: `fadeInUp 0.5s ease forwards`,
          },

          '@keyframes fadeInUp': {
            'to': {
              opacity: 1,
              transform: 'translateY(0)',
            }
          }
        }}
        rowHeight={40}
        disableColumnSelector
        disableColumnMenu
        rows={rows}
        columns={columns}
        hideFooter={true}
        {...props}
        slotProps={{
          loadingOverlay: {
            variant: 'skeleton',
            noRowsVariant: 'skeleton',
          },
        }}
      />
    </Box >
  );
}

export default Grid;
