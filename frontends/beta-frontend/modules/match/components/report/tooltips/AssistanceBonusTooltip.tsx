import { Grid } from '@mui/material';
import React from 'react';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import TooltipDataItem from './items/ToolTipDataItem';
import { ChainActionSequence } from '@/shared/models/match/Play';

interface AssistanceBonusTooltipProps {
  chainActionBonuses?: Record<ChainActionSequence, number>;
}

const AssistanceBonusTooltip: React.FC<AssistanceBonusTooltipProps> = ({
  chainActionBonuses,
}) => {
  console.log(chainActionBonuses);

  return (
    <Grid size={6}>
      {chainActionBonuses &&
        Object.entries(chainActionBonuses).map(([name, value]) => (
          <TooltipDataItem
            key={name}
            title={convertSnakeCaseToTitleCase(name)}
            value={value}
          />
        ))}
    </Grid>
  );
};

export default AssistanceBonusTooltip;
