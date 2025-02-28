import MessageText from './MessageText';
import { DENOMINATIONS_RANGES } from '@/shared/models/MatchReport';
import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import DoubleColTooltip from '../Tooltips/DoubleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import PitchAreaTooltip from '../Tooltips/PitchAreaTooltip';

const ShootMessage = ({
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

  return (
    <span style={{ fontSize: '12px', textAlign: 'justify' }}>
      In the{' '}
      <PitchAreaTooltip pitchArea={event.duel.pitchArea}>
        {convertSnakeCaseToTitleCase(duel.pitchArea)}
      </PitchAreaTooltip>{' '}
      area, attacker
      <MessageText
        children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
        matchEventSide={initiatorEventSide}
      />
      attempted a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText textColor={initColor}>{initLabel}</QualityText>
      </SingleColTooltip>{' '}
      <ActionText>{action}</ActionText>, Goalkeeper{' '}
      <MessageText
        children={`${duel.challenger?.name} [${getPositionInitials(duel.challenger?.position)}]`}
        matchEventSide={challengerEventSide}
      />
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText textColor={challColor}>{challLabel}</QualityText>
      </SingleColTooltip>{' '}
      save attempt, which led to{' '}
      <DoubleColTooltip
        attackerName={duel.initiator.name}
        defenderName={duel.challenger?.name}
        initiatorStats={duel.initiatorStats}
        challengerStats={duel.challengerStats}>
        {result === 'WIN' ? <>GOAL</> : <>NO GOAL</>}
      </DoubleColTooltip>
      {` [${event.homeScore}:${event.awayScore}]`}.
    </span>
  );
};

export default ShootMessage;
