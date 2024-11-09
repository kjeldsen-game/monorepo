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

const PassMessage = ({
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
      made a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText>
          {getRangeLabel(duel.initiatorStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>
      <ActionText>PASS</ActionText> to Reciever
      <MessageText
        children={`${duel.receiver?.name} [${getPositionInitials(duel.receiver?.position)}]`}
        matchEventSide={initiatorEventSide}
      />
    </span>
  );
};

export default PassMessage;
