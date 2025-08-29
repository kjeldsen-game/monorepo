import { render, screen } from '@testing-library/react';
import ActionText from '../ActionText';

describe('ActionText', () => {
    it('renders children normally when not SHOOT', () => {
        render(<ActionText>PASS</ActionText>);
        expect(screen.getByText(/pass/i)).toBeInTheDocument();
    });

    it('renders "SHOT" when children is SHOOT', () => {
        render(<ActionText>SHOOT</ActionText>);
        expect(screen.getByText(/shot/i)).toBeInTheDocument();
    });

    it('applies lowercase style', () => {
        render(<ActionText>POSITION</ActionText>);
        const element = screen.getByText(/position/i);
        expect(element).toHaveStyle({ textTransform: 'lowercase' });
    });
});
