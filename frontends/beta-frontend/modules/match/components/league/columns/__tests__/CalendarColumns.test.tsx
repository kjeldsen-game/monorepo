import { CalendarColumns } from '../CalendarColumns';
import { GridColDef } from '@mui/x-data-grid';

describe('CalendarColumns', () => {
    const teamId = 'team-1';

    it('should generate columns array with correct structure', () => {
        const columns: GridColDef[] = CalendarColumns(teamId);

        expect(Array.isArray(columns)).toBe(true);

        const fields = columns.map((col) => col.field);
        expect(fields).toEqual([
            'match',
            'home',
            'Date',
            'away',
            'result',
            'lineup',
            'actions',
        ]);

        columns.forEach((col) => {
            expect(typeof col.renderHeader).toBe('function');
        });

        const homeColumn = columns?.find((c) => c.field === 'home');
        expect(homeColumn?.renderCell).toBeDefined();

        const dummyRow = { home: { id: 'team-2', name: 'Team 2' }, away: { id: 'team-3', name: 'Team 3' }, status: 'SCHEDULED', id: 'match-1' };
        const actionsColumn = columns?.find((c) => c.field === 'actions');
        expect(actionsColumn?.renderCell?.({ row: dummyRow, id: 'match-1', api: { getRowIndexRelativeToVisibleRows: jest.fn() } } as any)).toBe(false);
    });

    it('should style links correctly when teamId matches', () => {
        const columns = CalendarColumns(teamId);
        const homeColumn = columns?.find((c) => c.field === 'home');

        const row = { home: { id: 'team-1', name: 'My Team' }, away: { id: 'team-2', name: 'Other Team' } };
        const rendered = homeColumn?.renderCell?.({ row, id: '1', api: { getRowIndexRelativeToVisibleRows: jest.fn() } } as any);

        expect(rendered.props.sx.color).toBe('#FF3F84');
    });
});
