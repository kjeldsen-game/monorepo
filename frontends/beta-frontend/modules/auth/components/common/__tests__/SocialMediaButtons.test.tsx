import { render, screen } from '@testing-library/react';
import SocialMediaButtons from '../SocialMediaButtons';
import '@testing-library/jest-dom';

describe('SocialMediaButtons', () => {
    it('renders all 3 social media links with icons', () => {
        render(<SocialMediaButtons />);

        expect(screen.getAllByRole('link')).toHaveLength(3);

        expect(screen.getByTestId('LinkedInIcon')).toBeInTheDocument();
        expect(screen.getByTestId('InstagramIcon')).toBeInTheDocument();
        expect(screen.getByTestId('TwitterIcon')).toBeInTheDocument();
    });
});
