import { Grid } from '@mui/material';
import React from 'react';
import TooltipDataCol from './TooltipDataCol';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import TooltipDataItem from './ToolTipDataItem';

interface AssistanceBonusTooltipProps {
  chainActionBonuses: any;
}

const AssistanceBonusTooltip: React.FC<AssistanceBonusTooltipProps> = ({
  chainActionBonuses,
}) => {
  console.log(chainActionBonuses);

  return (
    <Grid item xs={6}>
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
