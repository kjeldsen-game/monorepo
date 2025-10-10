import { render, screen } from '@testing-library/react';
import CalendarFilter from '../CalendarFilter';

jest.mock('@/shared/components/custom-accordion/CustomAccordion', () => ({
    __esModule: true,
    default: ({ children, title }) => (
        <div data-testid="mock-accordion">
            <div>{title}</div>
            {children}
        </div>
    ),
}));

jest.mock('@/shared/components/Common/CustomSelectInput', () => ({
    __esModule: true,
    default: (props) => (
        <div data-testid="mock-select" {...props}>
            Mock Select
        </div>
    ),
}));

describe('CalendarFilter', () => {
    it('renders CustomSelectInput inside mocked CustomAccordion', () => {
        const handleFilterChange = jest.fn();

        render(<CalendarFilter filter="Active" handleFilterChange={handleFilterChange} />);

        expect(screen.getByTestId('mock-accordion')).toBeInTheDocument();

        const select = screen.getByTestId('mock-select');
        expect(select).toBeInTheDocument();
        expect(select).toHaveAttribute('value', 'Active');
    });
});
