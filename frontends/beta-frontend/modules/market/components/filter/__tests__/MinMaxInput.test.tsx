import { render, screen, fireEvent } from '@testing-library/react';
import MinMaxInput from '../MinMaxInput';
import { MinMaxRange } from 'modules/market/types/filterForm';

describe('MinMaxInput', () => {
    const mockHandleChange = jest.fn();

    afterEach(() => {
        jest.clearAllMocks();
    });

    it('renders with basic MinMaxRange', () => {
        const values: MinMaxRange = { min: '1', max: '10' };

        render(
            <MinMaxInput
                minMaxFormValues={values}
                handleInputChange={mockHandleChange}
                inputName="Price"
            />
        );

        expect(screen.getByText('Price')).toBeInTheDocument();
        expect(screen.getByLabelText('Min')).toHaveValue(1);
        expect(screen.getByLabelText('Max')).toHaveValue(10);
    });

    it('renders with SkillRange and uses potentialMin/max if inputName includes "Potential"', () => {
        const values: MinMaxRange = {
            min: '1',
            max: '10',
            potentialMin: '5',
            potentialMax: '15',
        };

        render(
            <MinMaxInput
                minMaxFormValues={values}
                handleInputChange={mockHandleChange}
                inputName="Potential Skill"
            />
        );

        expect(screen.getByText('Potential Skill')).toBeInTheDocument();
        expect(screen.getByLabelText('Min')).toHaveValue(5);
        expect(screen.getByLabelText('Max')).toHaveValue(15);
    });

    it('calls handleInputChange when inputs change', () => {
        const values: MinMaxRange = { min: '2', max: '8' };

        render(
            <MinMaxInput
                minMaxFormValues={values}
                handleInputChange={mockHandleChange}
                inputName="Volume"
            />
        );

        const minInput = screen.getByLabelText('Min');
        const maxInput = screen.getByLabelText('Max');

        fireEvent.change(minInput, { target: { value: '3' } });
        fireEvent.change(maxInput, { target: { value: '9' } });

        expect(mockHandleChange).toHaveBeenCalledTimes(2);
    });
});
