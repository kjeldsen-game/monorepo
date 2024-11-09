import React from 'react';
import MessageText from './MessageText';
import {
  Action,
  DENOMINATIONS_RANGES,
  Duel,
  MatchEventSide,
  MatchResult,
} from '@/shared/models/MatchReport';
import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import CustomTooltip from '../CustomTooltip';
import { Box, Grid, Typography } from '@mui/material';
import TooltipDataCol from '../TooltipDataCol';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import DoubleColTooltip from '../Tooltips/DoubleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';

const ShootMessage = ({
  duel,
  initiatorEventSide,
  challengerEventSide,
  action,
  result,
}: MessageProps) => {
  return (
    <span style={{ fontSize: '12px' }}>
      Attacker
      <MessageText
        children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
        matchEventSide={initiatorEventSide}
      />
      attempted a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText>
          {getRangeLabel(duel.initiatorStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>{' '}
      <ActionText> {action}</ActionText>, Goalkeeper{' '}
      <MessageText
        children={`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
        matchEventSide={challengerEventSide}
      />
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText>
          {getRangeLabel(duel.challengerStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>{' '}
      save attempt, which led to{' '}
      <DoubleColTooltip
        attackerName={duel.initiator.name}
        defenderName={duel.challenger?.name}
        initiatorStats={duel.initiatorStats}
        challengerStats={duel.challengerStats}>
        {result === 'WIN' ? <>GOAL</> : <>NO GOAL</>}
      </DoubleColTooltip>
      .
    </span>
  );
};

export default ShootMessage;
