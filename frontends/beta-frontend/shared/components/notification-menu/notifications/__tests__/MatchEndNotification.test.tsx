import React from "react";
import { render, screen } from "@testing-library/react";
import MatchEndNotification from "../MatchEndNotification";

jest.mock("../common/CommonNotification", () => ({
    __esModule: true,
    default: ({ children, id }: any) => <div data-testid="common-notification">{children}</div>,
}));

describe("MatchEndNotification", () => {
    const notification: any = {
        id: "123",
        message: "Match has ended",
        payload: {
            matchId: "abc-456",
        },
    };

    it("renders the message and View link", () => {
        render(<MatchEndNotification notification={notification} />);

        expect(screen.getByText("Match has ended")).toBeInTheDocument();

        const link = screen.getByText("View");
        expect(link).toBeInTheDocument();
        expect(link.closest("a")).toHaveAttribute("href", "/match/report/abc-456");
    });
});
