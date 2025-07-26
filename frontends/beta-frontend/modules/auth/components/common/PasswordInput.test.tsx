import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { useForm } from 'react-hook-form';
import PasswordInput from './PasswordInput';

describe('PasswordInput', () => {
    const setup = () => {
        const TestComponent = () => {
            const { control, handleSubmit } = useForm();
            return (
                <form onSubmit={handleSubmit(() => { })}>
                    <PasswordInput
                        name="password"
                        label="Password"
                        control={control}
                        rules={{ required: 'Password is required!' }}
                    />
                    <button type="submit">Submit</button>
                </form>
            );
        };
        return render(<TestComponent />);
    };

    const getInputByLabel = (label: RegExp) => {
        const elements = screen.getAllByLabelText(label);
        const input = elements.find(el => el.tagName.toLowerCase() === 'input');
        if (!input) throw new Error(`Input with label ${label} not found`);
        return input as HTMLInputElement;
    };

    test('renders with label', () => {
        setup();
        const input = getInputByLabel(/password/i);
        expect(input).toBeInTheDocument();
    });

    test('toggles password visibility on icon click', () => {
        setup();
        const input = getInputByLabel(/password/i);
        const toggleButton = screen.getByLabelText(/toggle password visibility/i);

        expect(input.type).toBe('password');

        fireEvent.click(toggleButton);
        expect(input.type).toBe('text');

        fireEvent.click(toggleButton);
        expect(input.type).toBe('password');
    });

    test('shows required error when submitted empty', async () => {
        setup();

        const submitBtn = screen.getByRole('button', { name: /submit/i });
        fireEvent.click(submitBtn);

        await waitFor(() => {
            expect(screen.getByText(/password is required!/i)).toBeInTheDocument();
        });
    });

    test('does not show error when input has value', async () => {
        setup();

        const input = getInputByLabel(/password/i);
        const submitBtn = screen.getByRole('button', { name: /submit/i });

        fireEvent.change(input, { target: { value: 'mySecret123' } });
        fireEvent.click(submitBtn);

        await waitFor(() => {
            expect(screen.queryByText(/password is required!/i)).not.toBeInTheDocument();
        });
    });
});
