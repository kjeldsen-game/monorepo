import { render, screen } from "@testing-library/react";
import LeagueStartNotification from "../LeagueStartNotification";

jest.mock("../common/CommonNotification", () => ({
    __esModule: true,
    default: ({ children, id }: any) => <div data-testid="common-notification">{children}</div>,
}));

describe("LeagueStartNotification", () => {
    const notification: any = {
        id: "123",
        message: "League has started....",
        payload: null,
    };

    it("renders the message and View link", () => {
        render(<LeagueStartNotification notification={notification} />);

        expect(screen.getByText("League has started....")).toBeInTheDocument();

        const link = screen.getByText("View");
        expect(link).toBeInTheDocument();
        expect(link.closest("a")).toHaveAttribute("href", "/league");
    });
});
