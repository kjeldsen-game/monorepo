import { DataGrid, DataGridProps, GridColDef } from '@mui/x-data-grid';
import Box from '@mui/material/Box';

type GridProps<T> = {
  rows: T[];
  columns: GridColDef[];
} & DataGridProps;

function Grid<T>({ rows, columns, sx, ...props }: GridProps<T>) {
  return (
    <Box
      sx={{
        overflow: 'hidden',
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
          display: 'none !important',
        },
        '& .MuiDataGrid-columnHeaders': {
          fontWeight: 900,
          padding: '0px !important',
          borderBottom: 'none',

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

      }}>
      <DataGrid
        // style={{
        //   '--DataGrid-rowWidth': '800px',
        // }}
        pageSizeOptions={[]} // Hides the dropdown choices
        columnHeaderHeight={40}
        sx={{
          '& .MuiDataGrid-columnHeaders .MuiDataGrid-scrollbarFiller': {
            background: '#F3F4F6'
          },
          '& .MuiDataGrid-columnHeader': {
            background: '#F3F4F6 !important',
            color: "#555F6C",
            padding: '0px !important',
          },
          '& .MuiDataGrid-columnSeparator': {
            display: 'none !important',
          },
          '& .MuiDataGrid-columnHeaders': {
            fontWeight: 900,
            padding: '0px !important',

          },
          '--DataGrid-rowBorderColor': 'transparent',
          '& .MuiDataGrid-footerContainer': {
            borderTop: '0 solid red',
          },
          '& .MuiTablePagination-toolbar': {
            display: 'flex',
            justifyContent: 'space-between',
            paddingLeft: 2,
            paddingRight: 2,
          },
          '& .MuiTablePagination-displayedRows': {
            color: '#555F6C',
            order: -1, // move to start
            marginRight: 'auto',
          },
          '& .MuiTablePagination-actions': {
            marginLeft: 'auto',
          },
          '& .MuiTablePagination-selectLabel, & .MuiTablePagination-select': {
            display: 'none', // optional: hide "rows per page"
          },
          '& .MuiDataGrid-row': {
            borderBottom: '0.5px solid rgba(183, 182, 182, 0.85)',
            borderTop: '0px',
            transform: 'translateY(10px)',
            animation: `fadeInUp 0.5s ease forwards`,
            boxSizing: 'border-box'
          },
          '& .MuiDataGrid-cell': {
            padding: '0px !important',
          },
          border: 'none',
          '@keyframes fadeInUp': {
            'to': {
              opacity: 1,
              transform: 'translateY(0)',
            }
          },
          ...(sx || {}),
        }}
        rowHeight={40}
        disableColumnSelector
        disableColumnMenu
        rows={rows}
        disableRowSelectionOnClick
        columns={columns}
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
