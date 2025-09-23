import { StandingsColumns } from '../StandingsColumns';
import { GridColDef } from '@mui/x-data-grid';
import { render } from '@testing-library/react';

describe('StandingsColumns', () => {

    const dummyRow = {
        position: 1,
        wins: 10,
        draws: 5,
        losses: 3,
        gamesPlayed: 18,
        goalsScored: 25,
        goalsReceived: 10,
        points: 35,
        name: 'Team A',
        id: 'team-1',
    };

    it('should generate correct columns', () => {
        const columns: GridColDef[] = StandingsColumns<any>();

        expect(columns.length).toBe(8);

        const expectedFields = ['Pos', 'Team', 'Wins', 'Draws', 'Losses', 'Games Played', 'score', 'Points '];
        const fields = columns.map((col) => col.field || col.headerName);
        expect(fields).toEqual(expectedFields);
    });

    it('should render Score column correctly', () => {
        const columns: GridColDef[] = StandingsColumns<any>();
        const scoreColumn = columns.find((c) => c.field === 'score');
        expect(scoreColumn).toBeDefined();

        // Render cell and check output
        const { container } = render(
            scoreColumn!.renderCell!({ row: dummyRow, id: '1', api: {} } as any)
        );

        expect(container.textContent).toBe('25-10');
    });
});
