import {
  Action,
  Duel,
  DuelResultRange,
  MatchEventSide,
  MatchResult,
  Play,
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
  event: Play;
}

export interface MessageProps {
  duel: Duel;
  initiatorEventSide: MatchEventSide;
  challengerEventSide: MatchEventSide;
  action: Action;
  result: MatchResult;
  event: Play;
}

export const getRangeLabel = (
  value: number | undefined,
  ranges: DuelResultRange[],
) => {
  if (!value) return { label: 'Unknown', color: '#000000' };
  if (value < ranges[0].min) {
    return { label: ranges[0].label, color: ranges[0].color };
  } else if (value > ranges[ranges.length - 1].max) {
    return {
      label: ranges[ranges.length - 1].label,
      color: ranges[ranges.length - 1].color,
    };
  }

  const range = ranges.find((r) => value >= r.min && value <= r.max);
  return range
    ? { label: range.label, color: range.color }
    : { label: 'Unknown', color: '#000000' };
};

const MatchReportItemMessage: React.FC<MatchReportItemMessageProps> = ({
  event,
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
          event={event}
          initiatorEventSide={eventSides.initiatorEventSide}
          challengerEventSide={eventSides.challengerEventSide}
          result={duel.result}
          action={type}
        />
      )}
      {type === 'POSITION' && (
        <PositionMessage
          event={event}
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
          event={event}
          initiatorEventSide={eventSides.initiatorEventSide}
          challengerEventSide={eventSides.challengerEventSide}
          result={duel.result}
          action={type}
        />
      )}
      {type === 'TACKLE' && (
        <TackleMessage
          duel={duel}
          event={event}
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
