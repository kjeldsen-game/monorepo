import MessageText from './common/MessageText';
import QualityText from './common/QualityText';
import ActionText from './common/ActionText';
import SingleColTooltip from '../tooltips/SingleColTooltip';
import DoubleColTooltip from '../tooltips/DoubleColTooltip';
import DoubleColAssistanceTooltip from '../tooltips/DoubleColAssistanceTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import {
  ASSISTANCE_RANGES,
  DENOMINATIONS_RANGES,
  POSITIONAL_RANGES,
} from '@/shared/models/match/MatchReport';
import { MessageProps } from '../matchEvents/MatchPlayMessage';
import { getRangeLabel } from 'modules/match/utils/MatchReportUtils';

const PositionMessage = ({
  duel,
  initiatorEventSide,
  challengerEventSide,
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
    duel.initiatorStats.assistance?.adjusted -
    duel.challengerStats.assistance?.adjusted,
    ASSISTANCE_RANGES,
  );
  console.log(duel.initiatorStats)
  return (
    <>
      {duel.challenger != null ? (
        <span style={{ fontSize: '12px', textAlign: 'justify' }}>
          <MessageText
            children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
            matchEventSide={initiatorEventSide}
          />
          tried with a
          <SingleColTooltip stats={duel.initiatorStats} showAll={true}>
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
          .
        </span>
      ) : (
        // TODO will be probably removed as the positional duel cannot be withuot challenger
        <span style={{ fontSize: '12px', textAlign: 'justify' }}>
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
        </span>
      )}
    </>
  );
};

export default PositionMessage;
