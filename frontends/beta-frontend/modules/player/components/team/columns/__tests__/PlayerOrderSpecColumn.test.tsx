import { render, screen, fireEvent } from '@testing-library/react';
import { DataGrid } from '@mui/x-data-grid';
import { PlayerOrderSpecColumn } from '../PlayerOrderSpecColumn';
import { Player } from '@/shared/models/player/Player';
import { PitchArea, PitchAreaShortcuts } from 'modules/player/types/Player';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';

jest.mock('@/shared/components/Common/CustomSelectInput', () => (props: any) => {
    return (
        <select
            data-testid="custom-select"
            value={props.value}
            onChange={props.onChange}
        >
            {Object.keys(props.values).map((key) => (
                <option key={key} value={props.values[key]}>
                    {key}
                </option>
            ))}
        </select>
    );
});

describe('PlayerOrderSpecColumn', () => {
    const basePlayer: Partial<Player> = {
        id: '1',
        name: 'John Doe',
        playerOrder: PlayerOrder.PASS_TO_AREA,
        playerOrderDestinationPitchArea: PitchArea.CENTRE_BACK,
    };

    const getPlayerValue = (row: any) => row;
    const handlePlayerOrderSpecChange = jest.fn();

    const renderGrid = (player: Partial<Player>, isEditing = false) => {
        const columns = [
            PlayerOrderSpecColumn(getPlayerValue, 'center', handlePlayerOrderSpecChange, 'PO2', isEditing),
        ];

        render(
            <div style={{ height: 300, width: 600 }}>
                <DataGrid rows={[player]} columns={columns} getRowId={(row) => row.id} />
            </div>
        );
    };

    it('renders the column header correctly', () => {
        renderGrid(basePlayer);
        expect(screen.getByText('PO2')).toBeInTheDocument();
    });

    it('renders pitch area in non-edit mode', () => {
        renderGrid(basePlayer, false);
        expect(screen.getByText(PitchAreaShortcuts[basePlayer.playerOrderDestinationPitchArea])).toBeInTheDocument();
    });

    it('renders CustomSelectInput in edit mode for PASS_TO_AREA', () => {
        renderGrid(basePlayer, true);
        const select = screen.getByTestId('custom-select') as HTMLSelectElement;
        expect(select).toBeInTheDocument();
        expect(select.value).toBe(basePlayer.playerOrderDestinationPitchArea);
    });

    it('does not render CustomSelectInput for other player orders', () => {
        const otherPlayer = { ...basePlayer, playerOrder: PlayerOrder.CHANGE_FLANK } as Player;
        renderGrid(otherPlayer, true);
        expect(screen.queryByTestId('custom-select')).toBeNull();
    });

    it('calls handlePlayerOrderSpecChange on select change', () => {
        renderGrid(basePlayer, true);
        const select = screen.getByTestId('custom-select') as HTMLSelectElement;
        fireEvent.change(select, { target: { value: PitchArea.CENTRE_BACK } });
        expect(handlePlayerOrderSpecChange).toHaveBeenCalledWith(basePlayer, PitchArea.CENTRE_BACK);
    });
});
