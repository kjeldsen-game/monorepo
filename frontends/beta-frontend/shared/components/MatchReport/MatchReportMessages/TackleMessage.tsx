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
  const { label: initLabel, color: initColor } = getRangeLabel(
    duel.initiatorStats.total,
    DENOMINATIONS_RANGES,
  );

  const { label: challLabel, color: challColor } = getRangeLabel(
    duel.challengerStats.total,
    DENOMINATIONS_RANGES,
  );

  return duel.challenger === null ? (
    <span style={{ fontSize: '12px', textAlign: 'justify' }}>
      <MessageText matchEventSide={initiatorEventSide}>
        {`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
      </MessageText>{' '}
      was free to make
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText textColor={initColor}>{initLabel}</QualityText>
      </SingleColTooltip>
      dribble.
    </span>
  ) : (
    <span style={{ fontSize: '12px', textAlign: 'justify' }}>
      <MessageText matchEventSide={initiatorEventSide}>
        {`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
      </MessageText>{' '}
      attempted a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText textColor={initColor}>{initLabel}</QualityText>
      </SingleColTooltip>
      dribble,
      <MessageText matchEventSide={challengerEventSide}>
        {`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
      </MessageText>
      made a{' '}
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText textColor={challColor}>{challLabel}</QualityText>
      </SingleColTooltip>
      <ActionText> TACKLE</ActionText>,
      {result === 'WIN' ? (
        <> attacker controlled the ball.</>
      ) : (
        <> attacker lost the ball.</>
      )}
    </span>
  );
};

export default TackleMessage;
