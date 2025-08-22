import { render, screen, fireEvent } from '@testing-library/react';
import { DataGrid } from '@mui/x-data-grid';
import { PlayerOrderColumn } from '../PlayerOrderColumn';
import { Player } from '@/shared/models/player/Player';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';

jest.mock('@/shared/components/Common/CustomSelectInput', () => (props: any) => {
    return (
        <select data-testid="custom-select" value={props.value} onChange={props.onChange}>
            {Object.keys(props.values).map((key) => (
                <option key={key} value={props.values[key]}>
                    {key}
                </option>
            ))}
        </select>
    );
});

describe('PlayerOrderColumn', () => {
    const player: Player = {
        id: '1',
        name: 'John Doe',
        playerOrder: PlayerOrder.CHANGE_FLANK,
    } as unknown as Player;

    const getPlayerValue = (row: any) => row;

    const handlePlayerOrderChange = jest.fn();

    const renderGrid = (isEditing = false) => {
        const columns = [
            PlayerOrderColumn(getPlayerValue, 'center', handlePlayerOrderChange, 'PO', isEditing),
        ];

        render(
            <div style={{ height: 300, width: 600 }}>
                <DataGrid rows={[player]} columns={columns} getRowId={(row) => row.id} />
            </div>
        );
    };

    it('renders the column header correctly', () => {
        renderGrid();
        expect(screen.getByText('PO')).toBeInTheDocument();
    });

    it('renders player order in non-edit mode', () => {
        renderGrid(false);
        expect(screen.getByText(/Change Flank/i)).toBeInTheDocument();
    });

    it('renders CustomSelectInput in edit mode', () => {
        renderGrid(true);
        const select = screen.getByTestId('custom-select') as HTMLSelectElement;
        expect(select).toBeInTheDocument();
        expect(select.value).toBe(player.playerOrder);
    });

    it('calls handlePlayerOrderChange on select change', () => {
        renderGrid(true);
        const select = screen.getByTestId('custom-select') as HTMLSelectElement;
        fireEvent.change(select, { target: { value: PlayerOrder.CHANGE_FLANK } });
        expect(handlePlayerOrderChange).toHaveBeenCalledWith(player, PlayerOrder.CHANGE_FLANK);
    });
});
