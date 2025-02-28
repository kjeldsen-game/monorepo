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

  return (
    <span style={{ fontSize: '12px', textAlign: 'justify' }}>
      In{' '}
      <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
        {convertSnakeCaseToTitleCase(event.duel.pitchArea)}
      </PitchAreaTooltip>{' '}
      area
      <MessageText
        children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
        matchEventSide={initiatorEventSide}
      />{' '}
      attempted a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText textColor={initColor}>{initLabel}</QualityText>
      </SingleColTooltip>
      dribble,
      <MessageText
        children={`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
        matchEventSide={challengerEventSide}
      />
      made a{' '}
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText textColor={challColor}>{challLabel}</QualityText>
      </SingleColTooltip>
      <ActionText>TACKLE</ActionText>,
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
