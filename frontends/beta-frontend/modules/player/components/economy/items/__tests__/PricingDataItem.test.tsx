import { render, screen, fireEvent } from '@testing-library/react';
import PricingDataItem from '../PricingDataItem';

describe('PricingDataItem', () => {
    const mockAdd = jest.fn();
    const mockRemove = jest.fn();

    const defaultProps = {
        title: 'test_title',
        value: 1234.56,
        isEdit: false,
        handleAddButton: mockAdd,
        handleRemoveButton: mockRemove,
    };

    beforeEach(() => {
        jest.clearAllMocks();
    });

    it('renders title and formatted value correctly', () => {
        render(<PricingDataItem {...defaultProps} />);

        expect(screen.getByText('Test Title:')).toBeInTheDocument();
        expect(screen.getByText('1,234.56 $')).toBeInTheDocument();
    });

    it.each([
        [false, 0],
        [true, 2],
    ])('renders %i buttons when isEdit is %s', (isEdit, expectedButtonCount) => {
        render(<PricingDataItem {...defaultProps} isEdit={isEdit} />);
        const buttons = screen.queryAllByRole('button');
        expect(buttons.length).toBe(expectedButtonCount);
    });


    it('calls handleAddButton with correct arguments', () => {
        render(<PricingDataItem {...defaultProps} isEdit={true} />);
        const addButton = screen.getAllByRole('button')[0];
        fireEvent.click(addButton);
        expect(mockAdd).toHaveBeenCalledWith('test_title', 1234.56);
    });

    it('calls handleRemoveButton with correct arguments', () => {
        render(<PricingDataItem {...defaultProps} isEdit={true} />);
        const removeButton = screen.getAllByRole('button')[1];
        fireEvent.click(removeButton);
        expect(mockRemove).toHaveBeenCalledWith('test_title', 1234.56);
    });
});
