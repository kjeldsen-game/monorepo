import MessageText from './MessageText';
import {
  ASSISTANCE_RANGES,
  DENOMINATIONS_RANGES,
  POSITIONAL_RANGES,
} from '@/shared/models/MatchReport';
import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import DoubleColTooltip from '../Tooltips/DoubleColTooltip';
import DoubleColAssistanceTooltip from '../Tooltips/DoubleColAssistanceTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import PitchAreaTooltip from '../Tooltips/PitchAreaTooltip';

const PositionMessage = ({
  duel,
  initiatorEventSide,
  challengerEventSide,
  action,
  result,
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

  const { label: posLabel, color: posColor } = getRangeLabel(
    duel.challenger != null
      ? duel.initiatorStats.total - duel.challengerStats.total
      : duel.initiatorStats.total,
    POSITIONAL_RANGES,
  );

  const { label: assistLabel, color: assistColor } = getRangeLabel(
    duel.initiatorStats.assistance - duel.challengerStats.assistance,
    ASSISTANCE_RANGES,
  );

  return (
    <>
      {duel.challenger != null ? (
        <span style={{ fontSize: '12px', textAlign: 'justify' }}>
          In{' '}
          <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
            {convertSnakeCaseToTitleCase(event.duel.pitchArea)}
          </PitchAreaTooltip>{' '}
          area
          <MessageText
            children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
            matchEventSide={initiatorEventSide}
          />
          tried with a
          <SingleColTooltip stats={duel.initiatorStats}>
            <QualityText textColor={initColor}>{initLabel}</QualityText>
          </SingleColTooltip>
          <ActionText>EFFORT</ActionText> to get free,
          <MessageText
            children={`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
            matchEventSide={challengerEventSide}
          />{' '}
          made a
          <SingleColTooltip stats={duel.challengerStats}>
            <QualityText textColor={challColor}>{challLabel}</QualityText>
          </SingleColTooltip>
          attempt to stay close. The assistance battle was
          <DoubleColAssistanceTooltip
            attackerName={duel.initiator.name}
            defenderName={duel.challenger?.name}
            defenderStats={duel.challengerStats}
            attackerStats={duel.initiatorStats}>
            <QualityText textColor={assistColor}>{assistLabel}</QualityText>
          </DoubleColAssistanceTooltip>
          for the attacker and it all resulted that defender is
          <DoubleColTooltip
            attackerName={duel.initiator.name}
            defenderName={duel.challenger?.name}
            initiatorStats={duel.initiatorStats}
            challengerStats={duel.challengerStats}>
            <QualityText textColor={posColor}>{posLabel}</QualityText>
          </DoubleColTooltip>
          .{` [${event.homeScore}:${event.awayScore}]`}
        </span>
      ) : (
        <span style={{ fontSize: '12px', textAlign: 'justify' }}>
          In{' '}
          <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
            {convertSnakeCaseToTitleCase(event.duel.pitchArea)}
          </PitchAreaTooltip>{' '}
          area
          <MessageText
            children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
            matchEventSide={initiatorEventSide}
          />
          tried with a
          <SingleColTooltip stats={duel.initiatorStats}>
            <QualityText textColor={initColor}>{initLabel}</QualityText>
          </SingleColTooltip>
          <ActionText>EFFORT</ActionText> to get free. There was no challenger
          from oponent team so duel resulted in
          <SingleColTooltip stats={duel.initiatorStats}>
            <QualityText textColor={posColor}>{posLabel}</QualityText>
          </SingleColTooltip>
          .{` [${event.homeScore}:${event.awayScore}]`}
        </span>
      )}
    </>
  );
};

export default PositionMessage;
