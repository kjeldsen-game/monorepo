import { render } from '@testing-library/react';
import SkillRelevanceSummary from '../SkillRelevanceSummary';
import { PlayerSkillRelevance } from '@/shared/models/player/Player';

jest.mock('modules/player/utils/SkillRelevanceUtils', () => ({
    getSkillRelevanceColors: jest.fn(),
}));

import { getSkillRelevanceColors } from 'modules/player/utils/SkillRelevanceUtils';

describe('SkillRelevanceSummary', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    const testCases: [PlayerSkillRelevance, string][] = [
        [PlayerSkillRelevance.CORE, '#FF0000'],
        [PlayerSkillRelevance.RESIDUAL, '#00FF00'],
        [PlayerSkillRelevance.SECONDARY, '#0000FF'],
    ];

    it.each(testCases)(
        'renders correct color for %s relevance',
        (relevance, expectedColor) => {
            // @ts-ignore
            getSkillRelevanceColors.mockReturnValue({ start: expectedColor });

            const { container } = render(
                <SkillRelevanceSummary relevance={relevance}>99</SkillRelevanceSummary>
            );

            const box = container.firstChild as HTMLElement;
            expect(box).toHaveStyle(`color: ${expectedColor}`);

            // Check icon has same color
            const icon = container.querySelector('svg') as HTMLElement;
            expect(icon).toHaveStyle(`color: ${expectedColor}`);
        }
    );
});
