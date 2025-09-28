import React from "react";
import { render, screen } from "@testing-library/react";
import PlayerBidNotification from "../PlayerBidNotification";

jest.mock("../common/CommonNotification", () => ({
    __esModule: true,
    default: ({ children, id }: any) => <div data-testid="common-notification">{children}</div>,
}));

describe("PlayerBidNotification", () => {
    const notification: any = {
        id: "123",
        message: "New bid was placed on the player",
        payload: {
            playerId: "abc-456",
        },
    };

    it("renders the message and View link", () => {
        render(<PlayerBidNotification notification={notification} />);

        expect(screen.getByText(notification.message)).toBeInTheDocument();

        const link = screen.getByText("View");
        expect(link).toBeInTheDocument();
        expect(link.closest("a")).toHaveAttribute("href", "/player/abc-456");
    });
});
