import { render, screen, fireEvent } from '@testing-library/react';
import React from 'react';
import TacticsView from '../TacticsView';
import { Tactic, VerticalPressure, HorizontalPressure, TeamModifiers } from '../../../../types/TeamModifiers';

jest.mock('@/shared/utils/TeamModifiersUtils', () => ({
    getModifierDescription: (value: string) => `Description of ${value}`,
}));

jest.mock('../TeamModifiersForm', () => (props: any) => (
    <div>
        <button
            data-testid="change-tactic"
            onClick={() => props.handleModifierChange('attack', 'tactic')}
        >
            Change Tactic
        </button>
    </div>
));

// Mock PressureDescriptionItem
jest.mock('../PressureDescriptionItem', () => (props: any) => (
    <div data-testid={`description-${props.name.replace(/\s/g, '-')}`}>
        {props.name}: {props.description}
    </div>
));

describe('TacticsView', () => {
    const mockHandleTeamModifierChange = jest.fn();

    const teamModifiers: TeamModifiers = {
        tactic: Tactic.CATENACCIO,
        horizontalPressure: HorizontalPressure.NO_HORIZONTAL_FOCUS,
        verticalPressure: VerticalPressure.HIGH_PRESSURE,
    };

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders TeamModifiersForm and PressureDescriptionItems', () => {
        render(
            <TacticsView
                teamModifiers={teamModifiers}
                handleTeamModifierChange={mockHandleTeamModifierChange}
            />
        );

        expect(screen.getByText('Change Tactic')).toBeInTheDocument();

        expect(screen.getByTestId('description-Tactic')).toHaveTextContent('Tactic: Description of CATENACCIO');
        expect(screen.getByTestId('description-Horizontal-Pressure')).toHaveTextContent('Horizontal Pressure: Description of NO_HORIZONTAL_FOCUS');
        expect(screen.getByTestId('description-Vertical-Pressure')).toHaveTextContent('Vertical Pressure: Description of HIGH_PRESSURE');
    });

    it('calls handleTeamModifierChange when TeamModifiersForm triggers change', () => {
        render(
            <TacticsView
                teamModifiers={teamModifiers}
                handleTeamModifierChange={mockHandleTeamModifierChange}
            />
        );

        const changeButton = screen.getByTestId('change-tactic');
        fireEvent.click(changeButton);

        expect(mockHandleTeamModifierChange).toHaveBeenCalledWith('attack', 'tactic');
    });
});
