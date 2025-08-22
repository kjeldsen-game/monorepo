import { render, screen } from "@testing-library/react";
import { EconomyColumns } from "../EconomyColumns"
import { convertSnakeCaseToTitleCase } from "@/shared/utils/StringUtils";

describe("economyColumns", () => {
    const NEGATIVE_COLOR = '#C51A1A';
    const POSITIVE_COLOR = 'black';

    const testNegativeColor = (field: string, value: number) => {
        const column = EconomyColumns().find(c => c.field === field);
        const Cell = column?.renderCell;
        if (!Cell) throw new Error(`Missing cell render for field ${field}`);

        const { container } = render(
            <>{Cell({ row: { [field]: value } } as any)}</>
        );
        expect(container.firstChild).toHaveStyle(`color: ${NEGATIVE_COLOR}`);

        const expectedText = `${value.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        })} $`;
        expect(screen.getByText(expectedText)).toBeInTheDocument();
    };

    const testPositiveColor = (field: string, value: number) => {
        const column = EconomyColumns().find(c => c.field === field);
        const Cell = column?.renderCell;
        if (!Cell) throw new Error(`Missing cell renderer for ${field}`);

        const { container } = render(
            <>{Cell({ row: { [field]: value } } as any)}</>
        );
        expect(container.firstChild).toHaveStyle(`color: ${POSITIVE_COLOR}`);

        const expectedText = `${value.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        })} $`;
        expect(screen.getByText(expectedText)).toBeInTheDocument();
    };

    it('should render columns with correct header names', () => {
        const columns = EconomyColumns();
        expect(columns.map(col => col.field)).toEqual([
            'concept',
            'thisWeekAmount',
            'thisSeasonAmount',
        ]);
    });

    it('renders thisWeek with red color when value is negative', () => {
        testNegativeColor('thisWeekAmount', -123.45);
    });

    it('renders thisWeek with black color when value is positive', () => {
        testPositiveColor('thisWeekAmount', 200);
    });

    it('renders thisSeason with red color when value is negative', () => {
        testNegativeColor('thisSeasonAmount', -789.99);
    });

    it('renders thisSeason with black color when value is positive', () => {
        testPositiveColor('thisSeasonAmount', 0);
    });

    it("does not apply color logic to concept column", () => {
        const conceptCol = EconomyColumns().find(c => c.field === 'concept');
        const Cell = conceptCol?.renderCell;
        const row = { context: 'some_value' };

        if (Cell) {
            const { container } = render(<>{Cell({ row } as any)}</>);
            expect(container.firstChild).not.toHaveStyle(`color: ${NEGATIVE_COLOR}`);
        }
    });

    const testCases: string[] = [
        'Sponsor',
        'Player Sale',
        'Player Purchase',
        'Attendance',
        'Merchandise',
        'Player Wage',
        'Building Upgrade',
        'Building Maintenance',
        'Restaurant',
        'Billboards',
        'Total Income',
        'Total Outcome',
        'Total Balance',
    ];

    it.each(testCases)('should apply correct padding for "%s"', (context) => {
        const conceptColumn = EconomyColumns().find(col => col.field === 'concept')!;
        const { getByText } = render(
            <>{conceptColumn.renderCell!({ row: { context } } as any)}</>
        );

        const expectedText = context.includes('Total')
            ? context
            : convertSnakeCaseToTitleCase(context);

        const element = getByText(expectedText).closest('div');
        const expectedPadding = context.includes('Total') ? '5px' : '10px';

        expect(element).toHaveStyle(`padding-left: ${expectedPadding}`);
        expect(element).toHaveStyle(`padding-right: ${expectedPadding}`);
    });
});
