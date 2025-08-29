import { render, screen } from '@testing-library/react';
import AuthFooter from '../AuthFooter';

jest.mock('../SocialMediaButtons', () => () => <div data-testid="social-media-buttons" />);

describe('AuthFooter', () => {
    it('renders footer text and social media buttons', () => {
        render(<AuthFooter />);

        expect(screen.getByText(/Kjeldsen 2023 · All rights reserved/i)).toBeInTheDocument();
        expect(screen.getByText(/hello@kjeldsen\.com/i)).toBeInTheDocument();
        expect(screen.getByTestId('social-media-buttons')).toBeInTheDocument();
    });
});
