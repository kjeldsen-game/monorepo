import { render, screen } from '@testing-library/react';
import { PlayerDetails } from '../PlayerDetails';
import { mockPlayers } from 'modules/player/__fixtures__/player.fixture';

jest.mock('../../skill-relevance/SkillRelevanceSummary', () => ({
    __esModule: true,
    default: ({ children }: any) => <div data-testid="skill-summary">{children}</div>,
}));

jest.mock('../../skill-relevance/SkillRelevanceDetails', () => ({
    __esModule: true,
    default: ({ skills }: any) => <div data-testid="skill-details">{skills.length}</div>,
}));

jest.mock('@mui/material/Avatar', () => (props: any) => <div data-testid="avatar">{props.alt}</div>);
jest.mock('@mui/icons-material/Elderly', () => () => <div data-testid="elderly-icon" />);

describe('PlayerDetails', () => {

    it('renders loading state when player is undefined', () => {
        render(<PlayerDetails player={undefined as any} />);
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });

    it('renders player details correctly', () => {
        render(<PlayerDetails player={mockPlayers[0]} />);

        // Check basic info
        expect(screen.getByText('Willy Treutel')).toBeInTheDocument();
        expect(screen.getByText('FORWARD')).toBeInTheDocument();
        expect(screen.getByText('25')).toBeInTheDocument();

        expect(screen.getByTestId('avatar')).toBeInTheDocument();

        expect(screen.getByTestId('elderly-icon')).toBeInTheDocument();

        const summaries = screen.getAllByTestId('skill-summary');
        expect(summaries.length).toBe(3);
        summaries.forEach((summary) => expect(summary).toHaveTextContent('88'));
    });
});
