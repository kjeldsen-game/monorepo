import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import MessageText from './MessageText';
import { DENOMINATIONS_RANGES } from '@/shared/models/MatchReport';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import PitchAreaTooltip from '../Tooltips/PitchAreaTooltip';

const PassMessage = ({ duel, initiatorEventSide, event }: MessageProps) => {
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
      in{' '}
      <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
        {convertSnakeCaseToTitleCase(event.duel.pitchArea)}
      </PitchAreaTooltip>{' '}
      area
      {`. [${event.homeScore}:${event.awayScore}]`}
    </span>
  );
};

export default PassMessage;
