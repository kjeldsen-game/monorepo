import { render, screen } from '@testing-library/react';
import BaseFilterSection from '../BaseFilterSection';
import { MarketFilterForm } from 'modules/market/types/filterForm';

jest.mock('../MinMaxInput', () => ({
    __esModule: true,
    default: ({ inputName }: { inputName: string }) => (
        <div data-testid="min-max-input">{inputName}</div>
    ),
}));

jest.mock('@/shared/components/Common/CustomSelectInput', () => ({
    __esModule: true,
    default: ({ value }: { value: string }) => (
        <div data-testid="custom-select">{value}</div>
    ),
}));

describe('BaseFilterSection', () => {
    const mockFormValues: MarketFilterForm = {
        playerOffer: { min: '1000', max: '5000' },
        playerAge: { min: '18', max: '30' },
        position: 'MID',
        skillRanges: {
            SC: { min: '', max: '', potentialMin: '', potentialMax: '' },
            OP: { min: '', max: '', potentialMin: '', potentialMax: '' },
            BC: { min: '', max: '', potentialMin: '', potentialMax: '' },
            PA: { min: '', max: '', potentialMin: '', potentialMax: '' },
            AE: { min: '', max: '', potentialMin: '', potentialMax: '' },
            CO: { min: '', max: '', potentialMin: '', potentialMax: '' },
            TA: { min: '', max: '', potentialMin: '', potentialMax: '' },
            DP: { min: '', max: '', potentialMin: '', potentialMax: '' },
        },
    };

    const mockHandleInputChange = jest.fn();
    const mockHandlePositionChange = jest.fn();

    it('renders MinMaxInput for playerOffer and playerAge', () => {
        render(
            <BaseFilterSection
                formValues={mockFormValues}
                handleInputChange={mockHandleInputChange}
                handlePositionChange={mockHandlePositionChange}
            />
        );

        const inputs = screen.getAllByTestId('min-max-input');
        expect(inputs).toHaveLength(2);
        expect(inputs[0]).toHaveTextContent('playerOffer');
        expect(inputs[1]).toHaveTextContent('playerAge');
    });

    it('renders CustomSelectInput with correct position value', () => {
        render(
            <BaseFilterSection
                formValues={mockFormValues}
                handleInputChange={mockHandleInputChange}
                handlePositionChange={mockHandlePositionChange}
            />
        );

        const select = screen.getByTestId('custom-select');
        expect(select).toHaveTextContent('MID');
    });
});
