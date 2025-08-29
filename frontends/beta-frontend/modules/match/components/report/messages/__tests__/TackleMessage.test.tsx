import { render, screen } from '@testing-library/react';
import TackleMessage from '../TackleMessage';
import { MessageProps } from '../../matchEvents/MatchPlayMessage';

// Mock child components
jest.mock('../common/MessageText', () => (props: any) => (
    <span data-testid="message-text">{props.children}</span>
));
jest.mock('../common/QualityText', () => (props: any) => (
    <span data-testid="quality-text">{props.children}</span>
));
jest.mock('../common/ActionText', () => (props: any) => (
    <span data-testid="action-text">{props.children}</span>
));
jest.mock('../../tooltips/SingleColTooltip', () => (props: any) => (
    <div data-testid="single-col-tooltip">{props.children}</div>
));

const baseProps: MessageProps = {
    initiatorEventSide: 'HomeTeamEvent',
    challengerEventSide: 'AwayTeamEvent',
    duel: {
        initiator: { name: 'John', teamRole: 'HOME', position: 'MID' },
        challenger: { name: 'Jane', teamRole: 'AWAY', position: 'DEF' },
        initiatorStats: { total: 50 },
        challengerStats: { total: 40 },
        type: 'NORMAL',
        duelDisruption: null,
        action: 'TACKLE',
        result: 'WIN',
    } as any,
};

describe('TackleMessage', () => {
    it('renders free dribble when challenger is null', () => {
        const propsWithoutChallenger = {
            ...baseProps,
            duel: { ...baseProps.duel, challenger: null, challengerStats: null },
        };
        render(<TackleMessage {...propsWithoutChallenger} />);

        expect(screen.getByText(/was free to make/i)).toBeInTheDocument();
        expect(screen.getByTestId('message-text')).toBeInTheDocument();
        expect(screen.getByTestId('single-col-tooltip')).toBeInTheDocument();
    });

    it('renders tackle attempt with WIN result', () => {
        render(<TackleMessage {...baseProps} />);

        expect(screen.getAllByTestId('message-text').length).toBe(2); // initiator + challenger
        expect(screen.getAllByTestId('quality-text').length).toBe(2); // initiator + challenger stats
        expect(screen.getByTestId('action-text')).toHaveTextContent('TACKLE');
        expect(screen.getByText(/attacker controlled the ball/i)).toBeInTheDocument();
    });

    it('renders tackle attempt with non-WIN result', () => {
        const loseProps = { ...baseProps, duel: { ...baseProps.duel, result: 'LOSE' } };
        render(<TackleMessage {...loseProps} />);

        expect(screen.getByText(/attacker lost the ball/i)).toBeInTheDocument();
    });
});
