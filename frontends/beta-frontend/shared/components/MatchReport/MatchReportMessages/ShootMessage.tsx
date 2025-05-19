import MessageText from './MessageText';
import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import DoubleColTooltip from '../Tooltips/DoubleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { DENOMINATIONS_RANGES } from '@/shared/models/match/MatchReport';
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

  if (duel.challengerStats == null) {
    return (
      <span style={{ fontSize: '12px', textAlign: 'justify' }}>
        <MessageText
          children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
          matchEventSide={initiatorEventSide}
        />
        attempted a
        <SingleColTooltip stats={duel.initiatorStats}>
          <QualityText textColor={initColor}>{initLabel}</QualityText>
        </SingleColTooltip>{' '}
        <ActionText>{action}</ActionText>, but missed the shot, which led total{' '}
        <QualityText textColor={'black'}>{<>NO GOAL</>}</QualityText>
      </span>
    );
  }

  const { label: challLabel, color: challColor } = getRangeLabel(
    duel.challengerStats.total,
    DENOMINATIONS_RANGES,
  );

  return (
    <span style={{ fontSize: '12px', textAlign: 'justify' }}>
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
      />{' '}
      made
      <SingleColTooltip stats={duel.challengerStats}>
        <QualityText textColor={challColor}>{challLabel}</QualityText>
      </SingleColTooltip>{' '}
      save attempt, which led to{' '}
      <DoubleColTooltip
        attackerName={duel.initiator.name}
        defenderName={duel.challenger?.name}
        initiatorStats={duel.initiatorStats}
        challengerStats={duel.challengerStats}>
        <QualityText textColor={'black'}>
          {result === 'WIN' ? <>GOAL</> : <>NO GOAL</>}
        </QualityText>
      </DoubleColTooltip>
      .{/* {` [${event.homeScore}:${event.awayScore}]`}. */}
    </span>
  );
};

export default ShootMessage;
