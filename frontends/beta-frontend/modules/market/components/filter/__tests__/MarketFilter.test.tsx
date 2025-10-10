import { render, screen } from '@testing-library/react';
import MarketFilter from '../MarketFilter';

jest.mock('../../../hooks/useMarketFilterForm', () => ({
    useMarketFilterForm: () => ({
        formValues: {
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
        },
        handleInputChange: jest.fn(),
        handlePositionChange: jest.fn(),
    }),
}));

jest.mock('../BaseFilterSection', () => ({
    __esModule: true,
    default: () => <div data-testid="base-filter-section" />,
}));

jest.mock('../SkillFilterSection', () => ({
    __esModule: true,
    default: () => <div data-testid="skill-filter-section" />,
}));

jest.mock('@/shared/components/Common/CustomButton', () => ({
    __esModule: true,
    default: ({ onClick }: { onClick: () => void }) => (
        <button onClick={onClick} data-testid="custom-button">
            Search
        </button>
    ),
}));

jest.mock('../../../utils/MarketUtils', () => ({
    createAuctionQueryFilter: jest.fn().mockReturnValue({ test: 'value' }),
}));

describe('MarketFilter', () => {

    it('renders BaseFilterSection and SkillFilterSection', () => {
        const mockSetFilter = jest.fn();

        render(<MarketFilter setFilter={mockSetFilter} />);

        expect(screen.getByTestId('base-filter-section')).toBeInTheDocument();
        expect(screen.getByTestId('skill-filter-section')).toBeInTheDocument();
    });

});
