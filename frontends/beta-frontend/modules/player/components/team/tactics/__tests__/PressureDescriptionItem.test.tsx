import { render, screen } from "@testing-library/react";
import PressureDescriptionItem from "../PressureDescriptionItem";
import { mockModifierDescriptionFull, mockModifierDescriptionMinimal } from "modules/player/__fixtures__/teamModifierDescription.fixture";

describe("PressureDescriptionItem", () => {
    it("renders the name in bold", () => {
        render(<PressureDescriptionItem name="High Press" />);
        expect(screen.getByText("High Press")).toBeInTheDocument();
    });

    it("renders effect when provided", () => {
        render(
            <PressureDescriptionItem
                name="High Press"
                description={mockModifierDescriptionFull}
            />
        );
        expect(screen.getByText(/Effect:/i)).toHaveTextContent(
            "Effect: Increase tempo and intensity"
        );
    });

    it("renders purpose, pros, and cons when provided", () => {
        render(
            <PressureDescriptionItem
                name="Counter Press"
                description={mockModifierDescriptionFull}
            />
        );

        expect(
            screen.getByText("Effect: Increase tempo and intensity")
        ).toBeInTheDocument();
        expect(
            screen.getByText("Purpose: Disrupt opponent build-up")
        ).toBeInTheDocument();
        expect(
            screen.getByText("Pros: Forces opponent mistakes")
        ).toBeInTheDocument();
        expect(
            screen.getByText("Cons: Leaves space behind defense")
        ).toBeInTheDocument();
    });

    it("does not render fields that are not provided", () => {
        render(
            <PressureDescriptionItem
                name="Mid Block"
                description={mockModifierDescriptionMinimal}
            />
        );

        // Effect, Purpose, Pros, Cons should not exist
        expect(screen.queryByText(/Effect:/i)).toBeNull();
        expect(screen.queryByText(/Purpose:/i)).toBeNull();
        expect(screen.queryByText(/Pros:/i)).toBeNull();
        expect(screen.queryByText(/Cons:/i)).toBeNull();
    });
});
