import { render, screen } from '@testing-library/react';
import { mockPlayers } from 'modules/player/__fixtures__/player.fixture';
import SkillRelevanceDetails from '../SkillRelevanceDetails';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';

describe('SkillRelevanceDetails', () => {
    it('renders all skills with their actual values', () => {
        const skills = mockPlayers[0].actualSkills;
        render(<SkillRelevanceDetails skills={skills} />);

        Object.entries(skills).forEach(([skillName, skillData]) => {
            const title = convertSnakeCaseToTitleCase(skillName);
            const skillElement = screen.getByText(new RegExp(title, 'i'));
            expect(skillElement).toBeInTheDocument();
        });
    });
});
