import { render, screen } from "@testing-library/react";
import { playerTransactionsColumns } from "./PlayerTransactionsColumns";

describe("playerTransactionsColumns", () => {

    const NEGATIVE_COLOR = '#C51A1A';
    const POSITIVE_COLOR = 'black';

    it("should render columns with correct header names", () => {
        const columns = playerTransactionsColumns();
        expect(columns.map(col => col.field)).toEqual([
            'name',
            'weekSummary',
            'seasonSummary'
        ])
    })

    it("does not apply color logic to name column", () => {
        const conceptCol = playerTransactionsColumns().find(c => c.field === 'name');
        const Cell = conceptCol?.renderCell;

        const row = {
            player: {
                id: 'player-123',
                name: 'Test Player',
            },
        };

        if (Cell) {
            const { container, getByText } = render(<>{Cell({ row } as any)}</>);

            expect(getByText('Test Player')).toBeInTheDocument();

            expect(container.firstChild).toHaveStyle(`color: black`);
            expect(container.firstChild).not.toHaveStyle(`color: ${NEGATIVE_COLOR}`);
        }
    });


    it.each([
        ['weekSummary', 1000, POSITIVE_COLOR],
        ['weekSummary', -1000, NEGATIVE_COLOR],
        ['seasonSummary', 1000, POSITIVE_COLOR],
        ['seasonSummary', -1000, NEGATIVE_COLOR],
    ])(
        'renders %s with correct color when value is %d',
        (field: string, value: number, expectedColor: string) => {
            const column = playerTransactionsColumns().find((c) => c.field === field);
            const Cell = column?.renderCell;

            if (!Cell) throw new Error(`Missing cell renderer for ${field}`);

            const row = {
                transactionSummary:
                    field === 'weekSummary'
                        ? { weekSummary: value }
                        : { seasonSummary: value },
            };
            const { container } = render(<>{Cell({ row } as any)}</>);

            expect(container.firstChild).toHaveStyle(`color: ${expectedColor}`);
            expect(
                screen.getByText(`${value.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                })} $`)
            ).toBeInTheDocument();
        }
    );


})