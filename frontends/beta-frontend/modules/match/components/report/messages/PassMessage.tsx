import MessageText from './common/MessageText';
import QualityText from './common/QualityText';
import ActionText from './common/ActionText';
import SingleColTooltip from '../tooltips/SingleColTooltip';
import { getPositionInitials } from '@/shared/utils/PlayerUtils';
import { DENOMINATIONS_RANGES } from '@/shared/models/match/MatchReport';
import { DuelType } from '@/shared/models/match/Play';

import DisruptionTooltip from '../tooltips/DisruptionTooltip';
import { getRangeLabel } from 'modules/match/utils/MatchReportUtils';
import { MessageProps } from '../matchEvents/MatchPlayMessage';

const PassMessage = ({ initiatorEventSide, duel }: MessageProps) => {
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
