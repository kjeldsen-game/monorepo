import { render, screen } from "@testing-library/react";
import CommonNotification, { CommonNotificationProps } from "../CommonNotification";

const mockHandleNotificationRead = jest.fn();

jest.mock("@/shared/hooks/useNotificationApi", () => ({
    useNotificationApi: () => ({
        handleNotificationRead: mockHandleNotificationRead,
    }),
}));

describe("CommonNotification", () => {
    const setup = (props: CommonNotificationProps = {}) => {
        return render(
            <CommonNotification {...props}>
                <span>Test notification message</span>
            </CommonNotification>
        );
    };

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it("renders children and timestamp", () => {
        setup({ id: "123" });

        expect(screen.getByText("Test notification message")).toBeInTheDocument();
        expect(screen.getByText(/25 minutes ago/i)).toBeInTheDocument();
    });

    it("does not show mark-read button by default", () => {
        setup({ id: "123" });

        const button = screen.getByTestId("mark-read-button");
        expect(button).toBeInTheDocument();
        expect(button).toHaveStyle("visibility: hidden");
    });
});
