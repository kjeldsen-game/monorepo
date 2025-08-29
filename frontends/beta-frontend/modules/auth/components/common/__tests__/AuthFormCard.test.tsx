import { render, screen } from '@testing-library/react';
import AuthFormCard from '../AuthFormCard';

describe('AuthFormCard', () => {
    it('renders children and applies correct border styles', () => {
        render(
            <AuthFormCard>
                <div data-testid="test-child">Test Content</div>
            </AuthFormCard>
        );

        const card = screen.getByText('Test Content').closest('.MuiPaper-root');

        expect(card).toBeInTheDocument();

        expect(card).toHaveStyle({
            border: '2px solid #FF3F84',
            borderRadius: '20px',
            width: '320px',
        });
    });
});
