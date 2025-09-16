// ProgressBar.test.tsx
import { render, screen } from "@testing-library/react";
import ProgressBar from "../ProgressBar";

const COLORS = {
    green: "#CEDD6F",
    yellow: "#F3CF72",
    red: "#F48989",
    gray: "#F1F1F1",
};

describe("ProgressBar", () => {
    it.each([
        [1, COLORS.green],
        [4, COLORS.green],
        [5, COLORS.yellow],
        [10, COLORS.yellow],
        [12, COLORS.red],
        [14, COLORS.red],
    ])("renders correct colors for days=%i", (days, expectedColor) => {
        render(<ProgressBar days={days} />);

        const cells = screen.getAllByTestId("progress-cell"); for (let i = 0; i < days; i++) {
            expect(cells[i]).toHaveStyle(`background-color: ${expectedColor}`);
        }
        for (let i = days; i < cells.length; i++) {
            expect(cells[i]).toHaveStyle(`background-color: ${COLORS.gray}`);
        }

    });
});
