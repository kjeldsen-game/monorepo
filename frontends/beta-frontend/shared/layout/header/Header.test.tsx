import { render, screen } from "@testing-library/react";
import Header from "./Header";
import { useSession } from "next-auth/react";

jest.mock('next/image', () => ({
    __esModule: true,
    default: (props: any) => <img {...props} />,
}));

jest.mock('./UserDropdown', () => () => <div data-testid="user-dropdown" />)

jest.mock('next-auth/react', () => ({
    useSession: jest.fn(),
}));

const mockedUseSession = useSession as jest.Mock;

describe("Header", () => {


    it("shows the menu icon on small screens", () => {
        mockedUseSession.mockReturnValue({
            status: "authenticated",
            data: { user: { name: "John Doe", email: "john@example.com" } },
        });

        window.innerWidth = 500;
        window.dispatchEvent(new Event("resize"));

        render(<Header isClosing={false} mobileOpen={false} setMobileOpen={jest.fn()} />)

        const menuButton = screen.getByTestId("menu-icon");
        expect(menuButton).toBeVisible();
    })

    it("hides the menu icon on bigger screens", () => {
        window.innerWidth = 1024;
        window.dispatchEvent(new Event('resize'));

        render(
            <Header isClosing={false} mobileOpen={false} setMobileOpen={jest.fn()} />
        );

        const menuButton = screen.getByTestId("menu-icon");
        expect(menuButton).toBeInTheDocument();
    })

    it("shows UserDropdown when user is authenticated", () => {
        mockedUseSession.mockReturnValue({
            status: "authenticated",
            data: { user: { name: "John Doe", email: "john@example.com" } },
        });

        render(<Header isClosing={false} mobileOpen={false} setMobileOpen={jest.fn()} />);

        expect(screen.getByTestId("user-dropdown")).toBeVisible();
        expect(screen.queryByRole("button", { name: /sign in/i })).not.toBeInTheDocument();
    });

    it("shows Sign In button when user is not authenticated", () => {
        mockedUseSession.mockReturnValue({
            status: "unauthenticated",
            data: null,
        });

        render(<Header isClosing={false} mobileOpen={false} setMobileOpen={jest.fn()} />);

        expect(screen.queryByTestId("user-dropdown")).not.toBeInTheDocument();
        expect(screen.getByTestId("signin-button")).toBeInTheDocument();
    });
})