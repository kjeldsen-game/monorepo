import React from 'react'
import PassMessage from '../messages/PassMessage'
import TackleMessage from '../messages/TackleMessage';
import ShootMessage from '../messages/ShootMessage';
import PositionMessage from '../messages/PositionMessage';
import { Action, Duel, MatchEventSide } from 'modules/match/types/MatchResponses';

interface MatchPlayMessageProps {
    duel: Duel;
    action: Action;
}

export interface MessageProps {
    initiatorEventSide: MatchEventSide;
    challengerEventSide?: MatchEventSide;
    duel: Duel;
}

const MatchPlayMessage: React.FC<MatchPlayMessageProps> = ({
    duel, action
}) => {

    const eventSides: {
        initiatorEventSide: MatchEventSide;
        challengerEventSide: MatchEventSide;
    } = {
        initiatorEventSide:
            duel.initiator.teamRole === 'HOME' ? 'HomeTeamEvent' : 'AwayTeamEvent',
        challengerEventSide:
            duel.challenger?.teamRole === 'HOME' ? 'HomeTeamEvent' : 'AwayTeamEvent',
    };

    const actionComponents: Partial<Record<Action, React.FC<MessageProps>>> = {
        PASS: PassMessage,
        POSITION: PositionMessage,
        SHOOT: ShootMessage,
        TACKLE: TackleMessage,
    };

    const Component = actionComponents[action];
    if (!Component) return null;

    return (
        <Component
            duel={duel}
            initiatorEventSide={eventSides.initiatorEventSide}
            challengerEventSide={eventSides.challengerEventSide}
        />
    );
}

export default MatchPlayMessage