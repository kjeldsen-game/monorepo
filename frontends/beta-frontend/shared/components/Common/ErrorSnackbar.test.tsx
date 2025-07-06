import { useError } from "@/shared/contexts/ErrorContext"
import { act, render, screen } from "@testing-library/react"
import ErrorSnackbar from "./ErrorSnackbar"

jest.mock("@/shared/contexts/ErrorContext", () => ({
    useError: jest.fn(),
}));

describe("ErrorSnackbar", () => {
    it("shows snackbar when error is present", () => {
        const mockSetError = jest.fn();
        (useError as jest.Mock).mockReturnValue({
            error: "Something went wrong",
            setError: mockSetError,
        });

        render(<ErrorSnackbar />);
        expect(screen.getByText("Something went wrong")).toBeInTheDocument();
    });

    it("calls setError(null) after autoHideDuration", () => {
        jest.useFakeTimers();
        const mockSetError = jest.fn();

        (useError as jest.Mock).mockReturnValue({
            error: "Auto-dismiss error",
            setError: mockSetError,
        });

        render(<ErrorSnackbar />);
        expect(screen.getByText("Auto-dismiss error")).toBeInTheDocument();

        // Advance timer
        act(() => {
            jest.advanceTimersByTime(1500);
        });

        expect(mockSetError).toHaveBeenCalledWith(null);
        jest.useRealTimers();
    });
});
