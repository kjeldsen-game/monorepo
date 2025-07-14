import { useEconomyRepository } from "@/pages/api/economy/useEconomyRepository";
import { render, screen } from "@testing-library/react";
import { useSession } from "next-auth/react";
import BillboardDialog from "./BillboardDialog";

jest.mock('@mui/icons-material/MonetizationOn', () => () => <div data-testid="money-icon" />);

jest.mock('next-auth/react', () => ({
    useSession: jest.fn(),
}));

jest.mock('@/pages/api/economy/useEconomyRepository', () => ({
    useEconomyRepository: jest.fn(),
}));

describe("BillboardDialog", () => {
    const mockClose = jest.fn();

    beforeEach(() => {
        jest.clearAllMocks();

        (useSession as jest.Mock).mockReturnValue({
            data: {
                user: { teamId: 'team-123' },
                accessToken: 'mock-token',
            },
            status: 'authenticated',
        });

        (useEconomyRepository as jest.Mock).mockReturnValue({
            signBillboadDeal: jest.fn().mockResolvedValue({ status: 200 }),
        });
    });

    it("renders the dialog with correct content", () => {
        render(<BillboardDialog open={true} handleClose={mockClose} />);

        expect(screen.getByText(/Choose your new Billboard Deal/i)).toBeInTheDocument();
        expect(screen.getAllByText(/Lorem ipsum que tu quieras/i).length).toBeGreaterThan(0);
        expect(screen.getAllByText(/Short|Medium|Long/i).length).toBeGreaterThanOrEqual(1);
    });
});
