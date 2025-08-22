import { render, screen } from '@testing-library/react';
import { TransferColumn } from '../TransferColumn';

describe('TransferColumn', () => {
    const NEGATIVE_COLOR = '#C51A1A';
    const POSITIVE_COLOR = 'black';

    const mockGetValue = (row: any) => row;

    const renderCell = (value: number, field: string = 'someAmount') => {
        const column = TransferColumn(mockGetValue, 'right', 'Header', field);
        return render(<>{column.renderCell!({ row: { [field]: value } } as any)}</>);
    };

    it('renders negative transfer values in red', () => {
        const value = -123.45;
        const field = 'someAmount';
        const { container } = renderCell(value, field);

        expect(container.firstChild).toHaveStyle(`color: ${NEGATIVE_COLOR}`);

        const expectedText = `${value.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        })} $`;

        expect(screen.getByText(expectedText)).toBeInTheDocument();
    });

    it('renders positive transfer values in black', () => {
        const value = 987.65;
        const field = 'someAmount';
        const { container } = renderCell(value, field);

        expect(container.firstChild).toHaveStyle(`color: ${POSITIVE_COLOR}`);

        const expectedText = `${value.toLocaleString(undefined, {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2,
        })} $`;

        expect(screen.getByText(expectedText)).toBeInTheDocument();
    });

    it('applies correct right padding when alignment is right', () => {
        const column = TransferColumn(mockGetValue, 'right', 'Header', 'someAmount');
        const { container } = render(
            <>{column.renderCell!({ row: { someAmount: 100 } } as any)}</>
        );
        expect(container.firstChild).toHaveStyle('padding-right: 10px');
    });

    it('applies no right padding when alignment is not right', () => {
        const column = TransferColumn(mockGetValue, 'center', 'Header', 'someAmount');
        const { container } = render(
            <>{column.renderCell!({ row: { someAmount: 100 } } as any)}</>
        );
        expect(container.firstChild).toHaveStyle('padding-right: 0px');
    });
});
