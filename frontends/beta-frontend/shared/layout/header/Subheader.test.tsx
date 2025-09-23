import { render, screen } from "@testing-library/react";
import { useRouter } from "next/router";
import Subheader from "./Subheader";

jest.mock('next/router', () => ({
    useRouter: jest.fn(),
}));

jest.mock('next/link', () => (props: any) => props.children)

describe('Subheader component', () => {
    beforeAll(() => {
        (useRouter as jest.Mock).mockReturnValue({ pathname: '/foo/bar' });
    });

    it('renders with correct styles and content', () => {
        render(<Subheader />);
        const subheader = screen.getByTestId('subheader');
        const styles = window.getComputedStyle(subheader);

        expect(styles.backgroundColor).toBe('white');
        expect(styles.height).toBe('32px');

        expect(screen.getByText(/Welcome to/i)).toBeInTheDocument();
        expect(screen.getByText('KJELDSEN')).toBeInTheDocument();
        // expect(screen.getByText(/foo/i)).toBeInTheDocument();
        // expect(screen.getByText(/bar/i)).toBeInTheDocument();
    });
});