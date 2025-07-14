import { render, screen } from "@testing-library/react";
import ColLink from "./ColLink";
import { Typography } from "@mui/material";

describe('ColLink', () => {
    it('renders the link text', () => {
        render(<ColLink urlValue="/player/123"><Typography>test</Typography></ColLink>);
        const link = screen.getByRole('link', { name: /test/i });
        expect(link).toBeInTheDocument();
        expect(link).toHaveAttribute('href', '/player/123');
    });

    it('renders the link text, href and applies style props', () => {
        const customStyle = {
            color: 'red',
            textDecoration: 'underline',
            paddingInline: '10px',
            fontSize: '32px'
        };

        render(
            <ColLink urlValue="/player/123" sx={customStyle}>
                <Typography>test</Typography>
            </ColLink>
        );

        const link = screen.getByRole('link', { name: /test/i });
        expect(link).toBeInTheDocument();
        expect(link).toHaveAttribute('href', '/player/123');

        expect(link).toHaveStyle('padding-inline: 10px');
        expect(link).toHaveStyle('color: red');
        expect(link).toHaveStyle('text-decoration: underline');
        expect(link).toHaveStyle('fontSize: 32px');

    });
});