import { render, screen, fireEvent } from '@testing-library/react';
import MarketInputv2 from './MarketInput';

describe('MarketInputv2', () => {
    it('renders correctly with label and value', () => {
        render(
            <MarketInputv2
                value={10}
                label="Min"
                name="price"
                onChange={jest.fn()}
            />
        );

        const input = screen.getByLabelText('Min') as HTMLInputElement;
        expect(input).toBeInTheDocument();
        expect(input.value).toBe('10');
    });

    it('calls onChange when input value changes', () => {
        const handleChange = jest.fn();

        render(
            <MarketInputv2
                value={10}
                label="Max"
                name="price"
                onChange={handleChange}
            />
        );

        const input = screen.getByLabelText('Max');
        fireEvent.change(input, { target: { value: '20' } });

        expect(handleChange).toHaveBeenCalledTimes(1);
    });

    it('has type="number" to restrict input', () => {
        render(
            <MarketInputv2
                value={5}
                label="Min"
                name="amount"
                onChange={jest.fn()}
            />
        );

        const input = screen.getByLabelText('Min');
        expect(input).toHaveAttribute('type', 'number');
    });
});
