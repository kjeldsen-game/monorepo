import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import MessageText from './MessageText';
import { DENOMINATIONS_RANGES } from '@/shared/models/MatchReport';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import PitchAreaTooltip from '../Tooltips/PitchAreaTooltip';

const TackleMessage = ({
  duel,
  initiatorEventSide,
  challengerEventSide,
  result,
  action,
  event,
}: MessageProps) => {
  return (
    <span style={{ fontSize: '12px' }}>
      Defender
      <MessageText
        children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
        matchEventSide={initiatorEventSide}
      />{' '}
      , positioned in the{' '}
      <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
        {convertSnakeCaseToTitleCase(duel.pitchArea)}
      </PitchAreaTooltip>{' '}
      area, made a{' '}
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
      {` [${event.homeScore}:${event.awayScore}]`}
    </span>
  );
};

export default TackleMessage;
