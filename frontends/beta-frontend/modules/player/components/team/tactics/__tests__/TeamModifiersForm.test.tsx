import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import TeamModifiersForm from '../TeamModifiersForm';

// Mock useMediaQuery
import { useMediaQuery } from '@mui/material';
jest.mock('@mui/material', () => {
    const actual = jest.requireActual('@mui/material');
    return {
        ...actual,
        useMediaQuery: jest.fn(),
    };
});
const mockedUseMediaQuery = useMediaQuery as jest.Mock;

jest.mock('@/shared/components/Common/CustomSelectInput', () => (props: any) => {
    return (
        <select
            data-testid={props.title}
            value={props.value}
            onChange={(e) => props.onChange(e)}
        >
            {Object.keys(props.values).map((key: string) => (
                <option key={key} value={props.values[key]}>
                    {key}
                </option>
            ))}
        </select>
    );
});

describe('TeamModifiersForm', () => {
    const mockHandleModifierChange = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders all CustomSelectInput components', () => {
        mockedUseMediaQuery.mockReturnValue(false);

        render(
            <TeamModifiersForm
                teamModifiers={{ tactic: undefined, verticalPressure: undefined, horizontalPressure: undefined }}
                handleModifierChange={mockHandleModifierChange}
            />
        );

        expect(screen.getByTestId('Tactic')).toBeInTheDocument();
        expect(screen.getByTestId('Vertical Pressure')).toBeInTheDocument();
        expect(screen.getByTestId('Horizontal Pressure')).toBeInTheDocument();
    });
});
