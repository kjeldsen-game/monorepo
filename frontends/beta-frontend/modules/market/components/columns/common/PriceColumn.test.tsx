import React from 'react';
import { render, screen } from '@testing-library/react';
import { GridCellParams, GridRenderCellParams } from '@mui/x-data-grid';
import { PriceColumn } from './PriceColumn';

describe('PriceColumn', () => {
    const header = 'price';
    const getValue = (row: any) => row.price;

    it('should render header correctly', () => {
        const column = PriceColumn(getValue, 'center', header);
        render(<>{column.renderHeader?.()}</>);
        expect(screen.getByText('price')).toBeInTheDocument();
    });

    it('should render price with value correctly', () => {
        const column = PriceColumn(getValue, 'center', header);
        const row = { price: '99.99' };
        const params = { row } as GridRenderCellParams;

        const { container } = render(<>{column.renderCell?.(params)}</>);
        expect(container.textContent).toBe('99.99 $');
    });

    it('should render "0 $" when price is empty', () => {
        const column = PriceColumn(getValue, 'center', header);
        const row = { price: '' };
        const params = { row } as GridRenderCellParams;

        const { container } = render(<>{column.renderCell?.(params)}</>);
        expect(container.textContent).toBe('0 $');
    });

    it('should render "0 $" when price is undefined', () => {
        const column = PriceColumn(getValue, 'center', header);
        const row = {};
        const params = { row } as GridRenderCellParams;

        const { container } = render(<>{column.renderCell?.(params)}</>);
        expect(container.textContent).toBe('0 $');
    });
});
