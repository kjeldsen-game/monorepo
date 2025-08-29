import { render, screen } from '@testing-library/react';
import MatchPlay from '../MatchPlay';
import { Play } from 'modules/match/types/MatchResponses';

// Mock child components to simplify test
jest.mock('../MatchPlayMessage', () => (props: any) => (
    <div data-testid="match-play-message">{props.action}</div>
));
jest.mock('../../tooltips/PitchAreaTooltip', () => (props: any) => (
    <span data-testid="pitch-area-tooltip">{props.children}</span>
));

const basePlay: Play = {
    action: 'PASS',
    duel: {
        pitchArea: 'DEFENSIVE_THIRD',
    } as any,
    homeScore: 1,
    awayScore: 2,
} as Play;

describe('MatchPlay', () => {
    it('renders "In the area" text when index is 0', () => {
        render(<MatchPlay play={basePlay} index={0} isLast={false} />);
        expect(screen.getByText(/In the/i)).toBeInTheDocument();
        expect(screen.getByTestId('pitch-area-tooltip')).toHaveTextContent(
            'Defensive Third',
        );
    });

    it('does not render "In the area" when index is not 0', () => {
        render(<MatchPlay play={basePlay} index={1} isLast={false} />);
        expect(screen.queryByText(/In the/i)).not.toBeInTheDocument();
    });

    it('renders MatchPlayMessage with the play action', () => {
        render(<MatchPlay play={basePlay} index={0} isLast={false} />);
        expect(screen.getByTestId('match-play-message')).toHaveTextContent('PASS');
    });

    it('renders score when isLast is true', () => {
        render(<MatchPlay play={basePlay} index={0} isLast={true} />);
        expect(screen.getByText(/\[1:2\]/)).toBeInTheDocument();
    });

    it('does not render score when isLast is false', () => {
        render(<MatchPlay play={basePlay} index={0} isLast={false} />);
        expect(screen.queryByText(/\[1:2\]/)).not.toBeInTheDocument();
    });
});
