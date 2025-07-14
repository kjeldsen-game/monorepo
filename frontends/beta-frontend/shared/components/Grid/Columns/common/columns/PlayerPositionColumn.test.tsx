import { render } from '@testing-library/react';
import { PlayerPositionColumn, PlayerPositionType } from './PlayerPositionColumn';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { GridAlignment } from '@mui/x-data-grid';

jest.mock('@/shared/components/Player/PlayerPositionLabel', () => {
    return ({ position }: { position: PlayerPosition }) => (
        <div data-testid="player-position-label">{position}</div>
    );
});
jest.mock('../components/ColHeader', () => {
    return ({ header, align }: { header: string; align?: GridAlignment }) => (
        <div data-testid="col-header" data-align={align}>
            {header}
        </div>
    );
});

describe('PlayerPositionColumn', () => {
    const mockGetPlayerValue = (row: any) => row.player;

    const samplePlayer = {
        preferredPosition: PlayerPosition.GOALKEEPER,
        position: PlayerPosition.DEFENSIVE_MIDFIELDER,
    };

    it('returns a column definition with correct field and header', () => {
        const col = PlayerPositionColumn(mockGetPlayerValue, 'preferredPosition', 'Position Header');

        expect(col.field).toBe('preferredPosition');

        const { getByTestId } = render(col.renderHeader!({} as any));
        expect(getByTestId('col-header').textContent).toBe('Position Header');
    });

    it.each([
        ['left' as GridAlignment],
        ['center' as GridAlignment],
        ['right' as GridAlignment],
    ])('passes correct alignment (%s) to header and column config', (alignment) => {
        const col = PlayerPositionColumn(mockGetPlayerValue, 'preferredPosition', 'Pos', alignment);
        expect(col.align).toBe(alignment);
        expect(col.headerAlign).toBe(alignment);

        const { getByTestId } = render(col.renderHeader!({} as any));
        const header = getByTestId('col-header');
        expect(header.dataset.align).toBe(alignment);
    });

    const testCases: Array<[PlayerPositionType, PlayerPosition]> = [
        ['preferredPosition', PlayerPosition.GOALKEEPER],
        ['position', PlayerPosition.DEFENSIVE_MIDFIELDER],
    ];

    it.each(testCases)(
        'renderCell renders PlayerPositionLabel with correct position for %s',
        (posType, position) => {
            const col = PlayerPositionColumn(mockGetPlayerValue, posType);

            const params = {
                row: { player: samplePlayer },
            };

            const { getByTestId } = render(col.renderCell!(params as any));
            expect(getByTestId('player-position-label').textContent).toBe(position);
        }
    );
});
