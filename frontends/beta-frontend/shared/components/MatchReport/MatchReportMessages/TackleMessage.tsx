import React from 'react';
import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import MessageText from './MessageText';
import { DENOMINATIONS_RANGES } from '@/shared/models/MatchReport';
import CustomTooltip from '../CustomTooltip';
import { Box, Grid } from '@mui/material';
import TooltipDataCol from '../TooltipDataCol';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';

const TackleMessage = ({
  duel,
  initiatorEventSide,
  challengerEventSide,
  result,
  action,
}: MessageProps) => {
  return (
    <span style={{ fontSize: '12px' }}>
      Defender
      <MessageText
        children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
        matchEventSide={initiatorEventSide}
      />{' '}
      made a{' '}
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText>
          {getRangeLabel(duel.initiatorStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>{' '}
      <ActionText>TACKLE</ActionText>, Attacker
      <MessageText
        children={`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
        matchEventSide={challengerEventSide}
      />
      attempted a
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText>
          {getRangeLabel(duel.initiatorStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>
      dribble,
      {result === 'WIN' ? (
        <> attacker lost the ball.</>
      ) : (
        <> attacker controlled the ball.</>
      )}
    </span>
  );
};

export default TackleMessage;
