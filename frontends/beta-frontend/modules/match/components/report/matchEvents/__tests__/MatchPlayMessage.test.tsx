import { render, screen } from '@testing-library/react';
import MatchPlayMessage from '../MatchPlayMessage';
import { Action, Duel } from 'modules/match/types/MatchResponses';

// Mock child components
jest.mock('../../messages/PassMessage', () => (props: any) => (
    <div data-testid="pass-message">{props.duel.action}</div>
));
jest.mock('../../messages/PositionMessage', () => (props: any) => (
    <div data-testid="position-message">{props.duel.action}</div>
));
jest.mock('../../messages/ShootMessage', () => (props: any) => (
    <div data-testid="shoot-message">{props.duel.action}</div>
));
jest.mock('../../messages/TackleMessage', () => (props: any) => (
    <div data-testid="tackle-message">{props.duel.action}</div>
));

const baseDuel: Duel = {
    initiator: { name: 'John', teamRole: 'HOME', position: 'MID' },
    challenger: { name: 'Jane', teamRole: 'AWAY', position: 'DEF' },
    initiatorStats: { total: 50 },
    challengerStats: { total: 40 },
    type: 'NORMAL',
    duelDisruption: null,
    action: 'PASS',
} as any;

describe('MatchPlayMessage', () => {
    it('renders PassMessage for PASS action', () => {
        render(<MatchPlayMessage duel={baseDuel} action={Action.PASS} />);
        expect(screen.getByTestId('pass-message')).toBeInTheDocument();
    });

    it('renders PositionMessage for POSITION action', () => {
        render(<MatchPlayMessage duel={baseDuel} action={Action.POSITION} />);
        expect(screen.getByTestId('position-message')).toBeInTheDocument();
    });

    it('renders ShootMessage for SHOOT action', () => {
        render(<MatchPlayMessage duel={baseDuel} action={Action.SHOOT} />);
        expect(screen.getByTestId('shoot-message')).toBeInTheDocument();
    });

    it('renders TackleMessage for TACKLE action', () => {
        render(<MatchPlayMessage duel={baseDuel} action={Action.TACKLE} />);
        expect(screen.getByTestId('tackle-message')).toBeInTheDocument();
    });

    it('renders nothing for unknown action', () => {
        const { container } = render(<MatchPlayMessage duel={baseDuel} action={'UNKNOWN' as Action} />);
        expect(container.firstChild).toBeNull();
    });
});
