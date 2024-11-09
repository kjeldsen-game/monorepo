import {
  Action,
  Duel,
  DuelResultRange,
  MatchEventSide,
  MatchResult,
} from '@/shared/models/MatchReport';
import React from 'react';

import ShootMessage from './MatchReportMessages/ShootMessage';
import PassMessage from './MatchReportMessages/PassMessage';
import TackleMessage from './MatchReportMessages/TackleMessage';
import PositionMessage from './MatchReportMessages/PositionMessage';

interface MatchReportItemMessageProps {
  duel: Duel;
  type: any;
  homeId: any;
}

export interface MessageProps {
  duel: Duel;
  initiatorEventSide: MatchEventSide;
  challengerEventSide: MatchEventSide;
  action: Action;
  result: MatchResult;
}

export const getRangeLabel = (
  score: number,
  ranges: DuelResultRange[],
): string => {
  if (score < ranges[0].min) {
    return ranges[0].label;
  } else if (score > ranges[ranges.length - 1].max) {
    return ranges[ranges.length - 1].label;
  }
  return (
    ranges.find((range) => score >= range.min && score <= range.max)?.label ||
    'Unknown'
  );
};

const MatchReportItemMessage: React.FC<MatchReportItemMessageProps> = ({
  duel,
  type,
  homeId,
}) => {
  const eventSides: {
    initiatorEventSide: MatchEventSide;
    challengerEventSide: MatchEventSide;
  } = {
    initiatorEventSide:
      duel.initiator.teamId === homeId ? 'HomeTeamEvent' : 'AwayTeamEvent',
    challengerEventSide:
      duel.challenger?.teamId === homeId ? 'HomeTeamEvent' : 'AwayTeamEvent',
  };

  return (
    <>
      {type === 'PASS' && (
        <PassMessage
          duel={duel}
          initiatorEventSide={eventSides.initiatorEventSide}
          challengerEventSide={eventSides.challengerEventSide}
          result={duel.result}
          action={type}
        />
      )}
      {type === 'POSITION' && (
        <PositionMessage
          duel={duel}
          initiatorEventSide={eventSides.initiatorEventSide}
          challengerEventSide={eventSides.challengerEventSide}
          result={duel.result}
          action={type}
        />
      )}
      {type === 'SHOOT' && (
        <ShootMessage
          duel={duel}
          initiatorEventSide={eventSides.initiatorEventSide}
          challengerEventSide={eventSides.challengerEventSide}
          result={duel.result}
          action={type}
        />
      )}
      {type === 'TACKLE' && (
        <TackleMessage
          duel={duel}
          initiatorEventSide={eventSides.initiatorEventSide}
          challengerEventSide={eventSides.challengerEventSide}
          result={duel.result}
          action={type}
        />
      )}
    </>
  );
};

export default MatchReportItemMessage;
