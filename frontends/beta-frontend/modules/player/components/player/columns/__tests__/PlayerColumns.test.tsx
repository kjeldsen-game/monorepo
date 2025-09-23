import { render } from '@testing-library/react';
import { PlayerColumns } from '../PlayerColumns';
import { GridColDef } from '@mui/x-data-grid';

describe('PlayerColumns', () => {
    it('should generate all columns correctly', () => {
        const columns: GridColDef[] = PlayerColumns(false);

        expect(columns.length).toBe(8);

        const headers = columns.map((c) => c.field);
        console.log(columns)
        expect(headers).toEqual(['', 'GP', 'Goals', 'Assists', 'Tackles', 'Crd', 'Man of Match', 'Rating']);

        const alignments = columns.map((c) => c.align || 'left');
        expect(alignments).toEqual(['left', 'center', 'center', 'center', 'center', 'center', 'center', 'right']);
    });

    it('should render cell values correctly', () => {
        const columns: GridColDef[] = PlayerColumns(false);

        const rowData = {
            col1: 'Player 1',
            col2: 10,
            col3: 5,
            col4: 7,
            col5: 3,
            col6: 'Y',
            col7: 'Player 1',
            col8: 8.5,
        };

        const expectedValues = [
            'Player 1',
            "10",
            "5",
            "7",
            "3",
            'Y',
            'Player 1',
            "8.5",
        ];

        columns.forEach((col, index) => {
            const cellValue = col.valueGetter
                ? col.valueGetter({ row: rowData, id: '1', field: col.field, api: {} } as any)
                : col.renderCell
                    ? render(col.renderCell({ row: rowData, id: '1', field: col.field, api: {} } as any)).container.textContent
                    : undefined;

            expect(cellValue).toBe(expectedValues[index]);
        });
    });
});
