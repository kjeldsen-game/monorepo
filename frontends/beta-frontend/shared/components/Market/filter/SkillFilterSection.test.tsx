import { render, screen } from '@testing-library/react';
import SkillFilterSection from './SkillFilterSection';
import { SkillKey, SkillRanges } from 'hooks/useMarketFilterForm';

jest.mock('./SkillRowFilter', () => ({
    __esModule: true,
    default: jest.fn(({ namePrefix }) => (
        <div data-testid="skill-row-filter">{namePrefix || 'default'}</div>
    )),
}));

describe('SkillFilterSection', () => {
    const mockFormValues: SkillRanges = {
        SC: { min: '', max: '', potentialMin: '', potentialMax: '' },
        OP: { min: '', max: '', potentialMin: '', potentialMax: '' },
        BC: { min: '', max: '', potentialMin: '', potentialMax: '' },
        PA: { min: '', max: '', potentialMin: '', potentialMax: '' },
        AE: { min: '', max: '', potentialMin: '', potentialMax: '' },
        CO: { min: '', max: '', potentialMin: '', potentialMax: '' },
        TA: { min: '', max: '', potentialMin: '', potentialMax: '' },
        DP: { min: '', max: '', potentialMin: '', potentialMax: '' },
    };

    const mockHandleInputChange = jest.fn();

    it('renders two SkillRowFilter components', () => {
        render(
            <SkillFilterSection
                formValues={mockFormValues}
                handleInputChange={mockHandleInputChange}
            />
        );

        const filters = screen.getAllByTestId('skill-row-filter');
        expect(filters).toHaveLength(2);
        expect(filters[0]).toHaveTextContent('default');
        expect(filters[1]).toHaveTextContent('Potential');
    });
});
