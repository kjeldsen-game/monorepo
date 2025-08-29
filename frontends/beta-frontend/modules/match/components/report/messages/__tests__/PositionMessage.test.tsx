import { render, screen } from '@testing-library/react';
import PositionMessage from '../PositionMessage';
import { MessageProps } from '../../matchEvents/MatchPlayMessage';

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
jest.mock('../../tooltips/DoubleColAssistanceTooltip', () => (props: any) => (
    <div data-testid="double-col-assistance-tooltip">{props.children}</div>
));

const baseProps: MessageProps = {
    initiatorEventSide: 'HomeTeamEvent',
    challengerEventSide: 'AwayTeamEvent',
    duel: {
        initiator: { name: 'John', teamRole: 'HOME', position: 'MID' },
        challenger: { name: 'Jane', teamRole: 'AWAY', position: 'DEF' },
        initiatorStats: { total: 50, assistance: { adjusted: 10 } },
        challengerStats: { total: 40, assistance: { adjusted: 5 } },
        type: 'NORMAL',
        duelDisruption: null,
        action: 'POSITION',
    } as any,
};

describe('PositionMessage', () => {
    it('renders correctly when challenger exists', () => {
        render(<PositionMessage {...baseProps} />);

        expect(screen.getAllByTestId('message-text')).toHaveLength(2); // initiator + challenger
        expect(screen.getAllByTestId('quality-text').length).toBeGreaterThan(0);
        expect(screen.getAllByTestId('action-text')[0]).toHaveTextContent('EFFORT');
        expect(screen.getByTestId('double-col-tooltip')).toBeInTheDocument();
        expect(screen.getByTestId('double-col-assistance-tooltip')).toBeInTheDocument();
        expect(screen.getAllByTestId('single-col-tooltip').length).toBeGreaterThan(0);
    });

    it('renders correctly when challenger is null', () => {
        const propsWithoutChallenger = {
            ...baseProps,
            duel: {
                ...baseProps.duel,
                challenger: null,
            },
        };
        render(<PositionMessage {...propsWithoutChallenger} />);

        expect(screen.getAllByTestId('message-text')).toHaveLength(1); // only initiator
        expect(screen.getAllByTestId('quality-text').length).toBeGreaterThan(0);
        expect(screen.getAllByTestId('action-text')[0]).toHaveTextContent('EFFORT');
        expect(screen.queryByTestId('double-col-tooltip')).not.toBeInTheDocument();
        expect(screen.queryByTestId('double-col-assistance-tooltip')).not.toBeInTheDocument();
        expect(screen.getAllByTestId('single-col-tooltip').length).toBeGreaterThan(0);
    });
});
