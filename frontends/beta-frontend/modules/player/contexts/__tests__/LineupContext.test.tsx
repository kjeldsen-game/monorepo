import React from 'react';
import { render, screen } from '@testing-library/react';
import { LineupProvider, useLineupEdit } from '../LineupContext';
import { Player } from '@/shared/models/player/Player';

const TestComponent: React.FC = () => {
    const { handleEdit, activePlayer, edit } = useLineupEdit();
    return (
        <div>
            <button onClick={() => handleEdit({ id: '1', name: 'Player 1' } as Player)}>
                Edit
            </button>
            <span data-testid="activePlayer">{activePlayer?.name || 'none'}</span>
            <span data-testid="edit">{edit ? 'true' : 'false'}</span>
        </div>
    );
};

describe('LineupContext', () => {
    it('throws error when used outside provider', () => {
        const spy = jest.spyOn(console, 'error').mockImplementation(() => { });

        expect(() => render(<TestComponent />)).toThrow(
            'useLineupEdit must be used within a LineupProvider',
        );

        spy.mockRestore();
    });

    it('provides values to children inside LineupProvider', () => {
        const mockHandleEdit = jest.fn();
        const mockPlayer = { id: '99', name: 'Mock Player' } as Player;

        render(
            <LineupProvider handleEdit={mockHandleEdit} activePlayer={mockPlayer} edit={true}>
                <TestComponent />
            </LineupProvider>,
        );

        expect(screen.getByTestId('activePlayer')).toHaveTextContent('Mock Player');
        expect(screen.getByTestId('edit')).toHaveTextContent('true');

        screen.getByText('Edit').click();
        expect(mockHandleEdit).toHaveBeenCalledWith({ id: '1', name: 'Player 1' });
    });

    it('renders children correctly', () => {
        render(
            <LineupProvider
                handleEdit={() => { }}
                activePlayer={null}
                edit={false}
            >
                <div data-testid="child">Hello Child</div>
            </LineupProvider>,
        );

        expect(screen.getByTestId('child')).toBeInTheDocument();
        expect(screen.getByTestId('child')).toHaveTextContent('Hello Child');
    });
});
