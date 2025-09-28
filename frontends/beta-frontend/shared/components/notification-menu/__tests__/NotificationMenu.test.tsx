import React from "react";
import { render, screen, fireEvent } from "@testing-library/react";
import NotificationMenu from "../NotificationMenu";
import { useNotificationApi } from "@/shared/hooks/useNotificationApi";
import { NotificationType } from "@/shared/@types/responses";

jest.mock("@/shared/hooks/useNotificationApi");

const mockNotifications = [
    { id: "1", type: NotificationType.MATCH_END, message: "Match ended", payload: { "matchId": "123" } },
    { id: "2", type: NotificationType.AUCTION_BID, message: "Player bid", payload: { "playerId": "123" } },
];

describe("NotificationMenu", () => {
    beforeEach(() => {
        (useNotificationApi as jest.Mock).mockReturnValue({
            data: mockNotifications,
        });
    });

    // it("renders badge with correct number", () => {
    //     render(<NotificationMenu />);
    //     const badge = screen.getByText(mockNotifications.length.toString());
    //     expect(badge).toBeInTheDocument();
    // });

    // it("opens menu on bell click", () => {
    //     render(<NotificationMenu />);
    //     const bell = screen.getByRole("button");
    //     fireEvent.click(bell);
    //     expect(screen.getByText("Notifications")).toBeInTheDocument();
    // });

    it("renders notifications inside menu", () => {
        render(<NotificationMenu />);
        const bell = screen.getByRole("button");
        fireEvent.click(bell);

        // Check if notification messages are rendered
        mockNotifications.forEach((notification) => {
            expect(screen.getByText(notification.message)).toBeInTheDocument();
        });
    });
});
