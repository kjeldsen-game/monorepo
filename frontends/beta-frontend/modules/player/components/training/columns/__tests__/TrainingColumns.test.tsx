import { render, screen } from "@testing-library/react";
import { PlayerSkill } from "@/shared/models/player/PlayerSkill";
import { TrainingColumns } from "../TrainingColumns";

jest.mock("../../progressBar/ProgressBar", () => ({ days }: any) => (
    <div data-testid="progress-bar">{days}</div>
));

jest.mock("@/shared/utils/StringUtils", () => ({
    convertSnakeCaseToTitleCase: (str: string) => str.toUpperCase(),
}));

describe("TrainingColumns", () => {
    const columns = TrainingColumns(false);

    const mockRow = {
        player: { name: "John Doe", preferredPosition: "FW" },
        skill: PlayerSkill.AERIAL,
        pointsBeforeTraining: 5,
        pointsAfterTraining: 8,
        currentDay: 7,
    };

    it("renders correct headers", () => {
        columns.map((col) =>
            render(<>{col.renderHeader?.({} as any)}</>)
        );
        expect(screen.getByText("Name")).toBeInTheDocument();
        expect(screen.getByText("Pos")).toBeInTheDocument();
        expect(screen.getByText("Skill")).toBeInTheDocument();
        expect(screen.getByText("DV")).toBeInTheDocument();
        expect(screen.getByText("PB")).toBeInTheDocument();
    });

    it("renders cell values correctly", () => {
        const skillCol = columns.find((c) => c.field === "cv")!;
        const { container: skillCell } = render(
            <>{skillCol.renderCell?.({ row: mockRow } as any)}</>
        );
        expect(skillCell.textContent).toContain("AERIAL");

        const dvCol = columns.find((c) => c.field === "dv")!;
        const { container: dvCell } = render(
            <>{dvCol.renderCell?.({ row: mockRow } as any)}</>
        );
        expect(dvCell.textContent).toContain("5");
        expect(dvCell.textContent).toContain("8");

        const pbCol = columns.find((c) => c.field === "day")!;
        const { getByTestId } = render(
            <>{pbCol.renderCell?.({ row: mockRow } as any)}</>
        );
        expect(getByTestId("progress-bar")).toHaveTextContent("7");
    });
});
