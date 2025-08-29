import { render, screen } from '@testing-library/react';
import TooltipDataItem from '../ToolTipDataItem';

describe('TooltipDataItem', () => {
    it('renders title and value', () => {
        render(<TooltipDataItem title="Goals" value={5} />);

        expect(screen.getByText(/Goals:/)).toBeInTheDocument();
        expect(screen.getByText('5')).toBeInTheDocument();
    });

    it('renders with undefined value', () => {
        render(<TooltipDataItem title="Assists" value={undefined} />);
        expect(screen.getByText(/Assists:/)).toBeInTheDocument();
    });
});
