// __tests__/SocialMediaLink.test.tsx

import React from 'react';
import { render, screen } from '@testing-library/react';
import SocialMediaLink from './SocialMediaLink';
import '@testing-library/jest-dom';
import { Twitter } from '@mui/icons-material';
import userEvent from '@testing-library/user-event';

describe('SocialMediaLink', () => {
    it('renders child content', () => {
        render(
            <SocialMediaLink link="https://twitter.com">
                <Twitter data-testid="twitter-icon" />
            </SocialMediaLink>
        );

        expect(screen.getByTestId('twitter-icon')).toBeInTheDocument();
    });

    it('renders with correct href', () => {
        render(
            <SocialMediaLink link="https://twitter.com">
                <span>Twitter</span>
            </SocialMediaLink>
        );

        const link = screen.getByRole('link');
        expect(link).toHaveAttribute('href', 'https://twitter.com');
    });

    it('applies contained variant styles by default', () => {
        render(
            <SocialMediaLink link="/">
                <span data-testid="btn">Icon</span>
            </SocialMediaLink>
        );

        const box = screen.getByTestId('btn').parentElement;
        expect(box).toHaveStyle({
            backgroundColor: '#FF3F84',
            color: '#FFFFFF',
            border: '2px solid transparent',
        });
    });

    it('applies outlined variant styles when passed', () => {
        render(
            <SocialMediaLink link="/" variant="outlined">
                <span data-testid="btn">Icon</span>
            </SocialMediaLink>
        );

        const box = screen.getByTestId('btn').parentElement;
        expect(box).toHaveStyle({
            backgroundColor: '#FFFFFF',
            color: '#FF3F84',
            border: '2px solid #FF3F84',
        });
    });
});
