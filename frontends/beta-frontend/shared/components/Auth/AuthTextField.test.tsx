import React from 'react';
import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { useForm } from 'react-hook-form';
import AuthTextField from './AuthTextField';

const Wrapper = (props: any) => {
    const { control } = useForm({ mode: 'onBlur' });
    return <AuthTextField {...props} control={control} />;
};

describe('AuthTextField', () => {
    it('renders with label and default value', () => {
        render(<Wrapper name="first_name" defaultValue="John" />);
        expect(screen.getByLabelText('First Name')).toBeInTheDocument();
        expect(screen.getByDisplayValue('John')).toBeInTheDocument();
    });

    it('updates value on user input', async () => {
        render(<Wrapper name="email" defaultValue="" type="email" />);
        const input = screen.getByLabelText('Email');
        await userEvent.type(input, 'test@example.com');
        expect(input).toHaveValue('test@example.com');
    });

    it('shows validation error message', async () => {
        render(
            <Wrapper
                name="username"
                rules={{ required: 'Username is required' }}
            />
        );

        const input = screen.getByLabelText('Username');

        input.focus();
        input.blur();

        const errorMessage = await screen.findByText('Username is required');
        expect(errorMessage).toBeInTheDocument();
    });

});
