import { render, screen } from '@testing-library/react';
import PassMessage from '../PassMessage';
import { DuelType } from '@/shared/models/match/Play';
import { MessageProps } from '../../matchEvents/MatchPlayMessage';
import { DuelDisruptionType } from 'modules/match/types/MatchResponses';

const baseDuel = {
    initiator: { name: 'John Doe', position: 'MID', teamRole: 'HOME' },
    receiver: { name: 'Jane Smith', position: 'FWD', teamRole: 'HOME' },
    initiatorStats: { total: 50 },
    type: DuelType.PASSING_LOW,
    duelDisruption: null,
};

const baseProps: MessageProps = {
    duel: baseDuel as any,
    initiatorEventSide: 'HomeTeamEvent',
    challengerEventSide: 'AwayTeamEvent',
};

describe('PassMessage', () => {
    it('renders initiator and receiver names', () => {
        render(<PassMessage {...baseProps} />);
        expect(screen.getByText(/John Doe/)).toBeInTheDocument();
        expect(screen.getByText(/Jane Smith/)).toBeInTheDocument();
    });

    it('renders PASS action by default', () => {
        render(<PassMessage {...baseProps} />);
        expect(screen.getByText(/pass/i)).toBeInTheDocument();
    });

    it('renders THROW IN when duel type is THROW_IN', () => {
        const props = {
            ...baseProps,
            duel: { ...baseProps.duel, type: DuelType.THROW_IN },
        };
        render(<PassMessage {...props} />);
        expect(screen.getByText(/throw in from out/i)).toBeInTheDocument();
    });


    // export interface DuelDisruption {
    //   type: DuelDisruptionType;
    //   destinationPitchArea?: PitchArea;
    //   challenger?: Player;
    //   receiver?: Player;
    //   difference: number;
    //   random: number;
    // }


    it('renders disruption text with receiver if disruption exists', () => {
        const props = {
            ...baseProps,
            duel: {
                ...baseProps.duel,
                duelDisruption: {
                    type: DuelDisruptionType.MISSED_PASS,
                    random: 0.3,
                    difference: 5,
                    receiver: { name: 'Alex', position: 'DEF', teamRole: 'AWAY' } as any,
                },
            },
        };
        render(<PassMessage {...props} />);
        expect(screen.getByText(/missed/i)).toBeInTheDocument();
        expect(screen.getByText(/Alex/)).toBeInTheDocument();
    });

    it('renders disruption text without receiver if pass landed out', () => {
        const props = {
            ...baseProps,
            duel: {
                ...baseProps.duel,
                duelDisruption: {
                    random: 0.2,
                    difference: 3,
                    receiver: null,
                } as any,
            },
        };
        render(<PassMessage {...props} />);
        expect(screen.getByText(/missed/i)).toBeInTheDocument();
        expect(screen.getByText(/landed in out/i)).toBeInTheDocument();
    });
});
