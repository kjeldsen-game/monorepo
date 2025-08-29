import { render, screen } from '@testing-library/react';
import ShootMessage from '../ShootMessage';
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
jest.mock('../../tooltips/DoubleColTooltip', () => (props: any) => (
    <div data-testid="double-col-tooltip">{props.children}</div>
));

const baseProps: MessageProps = {
    initiatorEventSide: 'HomeTeamEvent',
    challengerEventSide: 'AwayTeamEvent',
    duel: {
        initiator: { name: 'John', teamRole: 'HOME', position: 'MID' },
        challenger: { name: 'Jane', teamRole: 'AWAY', position: 'GK' },
        initiatorStats: { total: 50 },
        challengerStats: { total: 40 },
        type: 'NORMAL',
        duelDisruption: null,
        action: 'SHOOT',
        result: 'WIN',
    } as any,
};

describe.skip('ShootMessage', () => {
    it('renders missed shot when challengerStats is null', () => {
        const propsWithoutChallenger = {
            ...baseProps,
            duel: { ...baseProps.duel, challengerStats: null },
        };
        render(<ShootMessage {...propsWithoutChallenger} />);

        expect(screen.getByText(/attempted a/i)).toBeInTheDocument();
        expect(screen.getAllByText(/SHOT/i)).toBeInTheDocument();
        expect(screen.getAllByText(/NO GOAL/i)).toBeInTheDocument();
        expect(screen.queryByTestId('double-col-tooltip')).not.toBeInTheDocument();
    });

    it('renders goalkeeping attempt when challengerStats exists', () => {
        render(<ShootMessage {...baseProps} />);

        expect(screen.getByText(/attempted a/i)).toBeInTheDocument();
        expect(screen.getAllByText(/SHOT/i)).toBeInTheDocument();
        expect(screen.getAllByText(/GOAL/i)).toBeInTheDocument();
        expect(screen.getByTestId('double-col-tooltip')).toBeInTheDocument();
        expect(screen.getAllByTestId('single-col-tooltip').length).toBeGreaterThan(0);
    });
});
