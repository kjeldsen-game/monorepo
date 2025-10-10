import { useMediaQuery } from '@mui/material';
import { StandingsColumns } from '../StandingsColumns';
import { GridColDef } from '@mui/x-data-grid';
import { render } from '@testing-library/react';

const mockedUseMediaQuery = useMediaQuery as jest.Mock;

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

    beforeEach(() => {
        jest.resetAllMocks();
    });

    it.skip('should return abbreviated headers when isXs is true', () => {
        // Simulate isXs being true
        mockedUseMediaQuery.mockReturnValue(true);

        const columns: GridColDef[] = StandingsColumns<any>();

        const expectedFieldsXs = ['Pos', 'Team', 'W', 'D', 'L', 'GP', 'score', 'Pts'];
        const fields = columns.map((col) => col.field || col.headerName);

        expect(fields).toEqual(expectedFieldsXs);
    });

    it('should return full headers when isXs is false', () => {
        // Simulate normal screen
        mockedUseMediaQuery.mockReturnValue(false);

        const columns: GridColDef[] = StandingsColumns<any>();

        const expectedFieldsFull = ['Pos', 'Team', 'Wins', 'Draws', 'Losses', 'Games Played', 'score', 'Points '];
        const fields = columns.map((col) => col.field || col.headerName);

        expect(fields).toEqual(expectedFieldsFull);
    });

    it('should render Score column correctly', () => {
        const columns: GridColDef[] = StandingsColumns<any>();
        const scoreColumn = columns.find((c) => c.field === 'score');
        expect(scoreColumn).toBeDefined();

        const { container } = render(
            scoreColumn!.renderCell!({ row: dummyRow, id: '1', api: {} } as any)
        );

        expect(container.textContent).toBe('25-10');
    });
});
