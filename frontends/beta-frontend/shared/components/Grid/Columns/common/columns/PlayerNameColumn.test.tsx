import React from 'react';
import { render } from '@testing-library/react';
import { PlayerNameColumn } from './PlayerNameColumn';

jest.mock('../components/ColHeader', () => ({ header }: { header: string }) => <div>{header}</div>);
jest.mock('../components/ColLink', () => ({ urlValue, children }: { urlValue: string; children: React.ReactNode }) => (
    <a href={urlValue} data-testid="col-link">
        {children}
    </a>
));

describe('PlayerNameColumns', () => {
    const samplePlayer = {
        id: '123',
        name: 'John Doe',
    };

    const mockGetPlayerValue = (row: any) => row.player;
    const col = PlayerNameColumn(mockGetPlayerValue);

    it('renders header correctly', () => {

        const { container } = render(<>{col.renderHeader && col.renderHeader({} as any)}</>);
        expect(container.textContent).toBe('Name');
    });
});
