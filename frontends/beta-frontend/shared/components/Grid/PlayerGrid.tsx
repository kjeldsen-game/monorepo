export type Row = [
  {
    id: number
    col1: string
    col2: number | undefined
    col3: number | undefined
    col4: number | undefined
    col5: number | undefined
    col6: number | undefined
    col7: number | undefined
    col8: number | undefined
  }
]

export type Column = [
  {
    field: string
    headerName: string
  }
]

export type GridProps = {
  rows: Row
  columns: Column
}

export const sampleRows = [
  { id: 1, col1: 'League', col2: '15', col3: '0', col4: '5', col5: '4', col6: '1', col7: '0', col8: '6.17' },
  { id: 2, col1: 'International', col2: '2', col3: '0', col4: '0', col5: '1', col6: '0', col7: '0', col8: '6' },
  { id: 3, col1: 'Friendly', col2: '0', col3: '0', col4: '0', col5: '0', col6: '0', col7: '0', col8: '/' },
]

export const sampleColumns = [
  { field: 'col1', headerName: '', width: 110 },
  { field: 'col2', headerName: 'GP', flex: 1 },
  { field: 'col3', headerName: 'Gls', flex: 1 },
  { field: 'col4', headerName: 'As', flex: 1 },
  { field: 'col5', headerName: 'Ta', flex: 1 },
  { field: 'col6', headerName: 'Crd', flex: 1 },
  { field: 'col7', headerName: 'MoM', flex: 1 },
  { field: 'col8', headerName: 'Rating', flex: 1 },
]
