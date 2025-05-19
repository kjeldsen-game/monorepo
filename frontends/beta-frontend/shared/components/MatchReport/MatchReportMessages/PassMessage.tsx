import { getRangeLabel, MessageProps } from '../MatchReportItemMessage';
import MessageText from './MessageText';
import QualityText from './QualityText';
import ActionText from './ActionText';
import SingleColTooltip from '../Tooltips/SingleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { DENOMINATIONS_RANGES } from '@/shared/models/match/MatchReport';
import { DuelDisruptionType, DuelType } from '@/shared/models/match/Play';
import { PITCH_AREAS_ENUM } from '@/shared/models/match/PitchArea';
import DisruptionTooltip from '../Tooltips/DisruptionTooltip';

const PassMessage = ({ duel, initiatorEventSide, event }: MessageProps) => {
  const { label, color } = getRangeLabel(
    duel.initiatorStats.total,
    DENOMINATIONS_RANGES,
  );

  return (
    <span style={{ fontSize: '12px', textAlign: 'justify' }}>
      <MessageText
        children={`${duel.initiator.name} [${getPositionInitials(duel.initiator.position)}]`}
        matchEventSide={initiatorEventSide}
      />
      made a
      <SingleColTooltip stats={duel.initiatorStats}>
        <QualityText textColor={color}>{label}</QualityText>
      </SingleColTooltip>
      {duel.type === DuelType.THROW_IN ? (
        <ActionText>THROW IN from out</ActionText>
      ) : (
        <ActionText>PASS</ActionText>
      )}
      to
      <MessageText
        children={`${duel.receiver?.name} [${getPositionInitials(duel.receiver?.position)}]`}
        matchEventSide={initiatorEventSide}
      />
      {duel.duelDisruption != null && (
        <>
          <DisruptionTooltip
            total={duel.initiatorStats.total}
            random={duel.duelDisruption.random}
            difference={duel.duelDisruption.difference}>
            missed{' '}
          </DisruptionTooltip>
          the pass{' '}
          {duel.duelDisruption.receiver != null ? (
            <>
              and landed to{' '}
              <MessageText
                children={`${duel.duelDisruption.receiver.name} [${getPositionInitials(duel.duelDisruption.receiver.position)}]`}
                matchEventSide={initiatorEventSide}
              />
            </>
          ) : (
            <>and landed in out.</>
          )}
        </>
      )}
    </span>
  );
};

export default PassMessage;
