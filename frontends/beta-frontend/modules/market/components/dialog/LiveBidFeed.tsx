import * as React from 'react';
import { DataGrid, GridToolbar } from '@mui/x-data-grid';
import { Box, Typography } from '@mui/material';

// --- 1. Hardcoded Data ---
const rows = [
  { id: 1, bidder: 'Your Current Bid', amount: 1500, time: '03/12/25, 10:30 PM', isCurrentBid: true },
  { id: 2, bidder: 'Team Rocket', amount: 1400, time: '03/12/25, 08:15 PM', isCurrentBid: false },
  { id: 3, bidder: '17261ddb-69ac-4e36-8994-5', amount: 0, time: '03/10/25, 12:00 PM', isCurrentBid: false },
];

// --- 2. Column Definitions ---
const columns = [
  { field: 'bidder', headerName: 'BIDDER', width: 400, headerClassName: 'header-style' },
  { 
    field: 'amount', 
    headerName: 'AMOUNT', 
    width: 200, 
    headerClassName: 'header-style',
    valueFormatter: (params) => {
      // Format the amount as $X
      return `${params.value} $`;
    },
    align: 'right', // Align content to the right
    headerAlign: 'right', // Align header text to the right
  },
  { field: 'time', headerName: 'TIME', width: 250, headerClassName: 'header-style' },
];

// --- 3. Component Definition ---
export default function LiveBidFeed() {
  return (
    <Box 
      sx={{ 
        height: 300, 
        width: '100%', 
        padding: 2, 
        backgroundColor: '#fff',
        borderRadius: '8px',
        boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)'
      }}
    >
      <Typography variant="h5" component="h1" gutterBottom sx={{ marginBottom: 2, fontWeight: 'bold' }}>
        Live Bid Feed
      </Typography>

      <DataGrid
        rows={rows}
        columns={columns}
        // Custom styling for the rows
        getRowClassName={(params) =>
          params.row.isCurrentBid ? 'current-bid-row' : ''
        }
        // General DataGrid configuration
        hideFooter={true} // Hides pagination/row count (like the image)
        disableColumnMenu={true} // Hides the column menu on hover
        disableRowSelectionOnClick={true} // Prevents row selection
        sx={{
          // Main styles to remove borders and align with the image
          border: 0,
          '& .MuiDataGrid-columnHeaders': {
            borderBottom: '1px solid #e0e0e0', // Subtle header separator
          },
          '& .MuiDataGrid-row': {
            borderBottom: 'none', // Remove row borders
            '&:hover': {
              backgroundColor: 'transparent', // Disable hover color
            },
          },
          // Custom style for the highlighted row
          '& .current-bid-row': {
            backgroundColor: '#ffdddd', // Light pink background
            '&:hover': {
              backgroundColor: '#ffdddd !important', // Ensure hover keeps the color
            },
          },
          // Custom header style (optional, for bolder look)
          '& .header-style': {
            fontWeight: 'bold',
            color: '#777',
            borderBottom: 'none',
          }
        }}
      />
    </Box>
  );
}