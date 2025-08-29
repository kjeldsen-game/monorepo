import { render, screen } from '@testing-library/react';
import MatchPossesion from '../MatchPossession';
import { Play } from 'modules/match/types/MatchResponses';

// Mock MatchPlay component
jest.mock('../MatchPlay', () => (props: any) => (
    <div data-testid="match-play">
        {props.index}-{props.isLast ? 'last' : 'not-last'}
    </div>
));

// Mock formatClock function
jest.mock('modules/match/utils/MatchReportUtils', () => ({
    formatClock: jest.fn((clock: number) => `Clock:${clock}`),
}));

describe('MatchPossesion', () => {
    const fakePossesion: Play[] = [
        { clock: 5, duel: { initiator: { teamRole: 'HOME' }, challenger: {} } } as any,
        { clock: 10, duel: { initiator: { teamRole: 'HOME' }, challenger: {} } } as any,
    ];

    it('renders the formatted clock from the first play', () => {
        render(<MatchPossesion possesion={fakePossesion} />);
        expect(screen.getByText(/Clock:5/i)).toBeInTheDocument();
    });

    it('renders a MatchPlay for each play with correct isLast', () => {
        render(<MatchPossesion possesion={fakePossesion} />);
        const matchPlays = screen.getAllByTestId('match-play');
        expect(matchPlays).toHaveLength(fakePossesion.length);

        expect(matchPlays[0]).toHaveTextContent('0-not-last');
        expect(matchPlays[1]).toHaveTextContent('1-last');
    });
});
