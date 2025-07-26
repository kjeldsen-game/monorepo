import { render, screen } from '@testing-library/react';
import AuthViewWrapper from './AuthViewWrapper';

jest.mock('./AuthFooter', () => () => <div data-testid="auth-footer" />);

describe('AuthViewWrapper', () => {
    it('renders children and footer correctly', () => {
        render(
            <AuthViewWrapper title="Sign In">
                <div data-testid="child-content">Hello Auth</div>
            </AuthViewWrapper>
        );

        expect(screen.getByTestId('child-content')).toHaveTextContent('Hello Auth');
        expect(screen.getByTestId('auth-footer')).toBeInTheDocument();
    });
});
