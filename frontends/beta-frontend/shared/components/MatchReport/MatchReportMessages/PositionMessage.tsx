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

const PositionMessage = ({
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
      tried with a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText>
          {getRangeLabel(duel.initiatorStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>
      <ActionText>EFFORT</ActionText> to get free, Defender
      <MessageText
        children={`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
        matchEventSide={challengerEventSide}
      />{' '}
      made a
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText>
          {getRangeLabel(duel.challengerStats.total, DENOMINATIONS_RANGES)}
        </QualityText>
      </SingleColTooltip>
      attempt to stay close, and was
      <DoubleColTooltip
        attackerName={duel.initiator.name}
        defenderName={duel.challenger?.name}
        initiatorStats={duel.initiatorStats}
        challengerStats={duel.challengerStats}>
        <QualityText>
          {getRangeLabel(
            duel.initiatorStats.total - duel.challengerStats.total,
            POSITIONAL_RANGES,
          )}
        </QualityText>
      </DoubleColTooltip>
      and the assistance battle resulted in
      <DoubleColAssistanceTooltip
        attackerName={duel.initiator.name}
        defenderName={duel.challenger?.name}
        defenderStats={duel.challengerStats}
        attackerStats={duel.initiatorStats}>
        <QualityText>
          {getRangeLabel(
            duel.initiatorStats.assistance - duel.challengerStats.assistance,
            ASSISTANCE_RANGES,
          )}
        </QualityText>
      </DoubleColAssistanceTooltip>
      assistance for the attacker .
    </span>
  );
};

export default PositionMessage;
