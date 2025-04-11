import { ReactNode } from 'react';
import CustomTooltip from './CustomTooltip';
import { Box, Grid, Typography } from '@mui/material';
import TooltipDataItem from './ToolTipDataItem';
import { RStats } from '@/shared/models/MatchReport';
import { formatName } from '@/shared/utils/MatchReportUtils';
import AssistanceBonusTooltip from './AssistanceBonusTooltip';

interface DoubleColAssistanceTooltipProps {
  attackerName: string;
  defenderName?: string;
  attackerStats: RStats;
  defenderStats: RStats;
  children: ReactNode;
}

const DoubleColAssistanceTooltip = ({
  attackerName,
  defenderName,
  attackerStats,
  defenderStats,
  children,
}: DoubleColAssistanceTooltipProps) => {
  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Grid container>
            <Grid>
              <Typography>{formatName(attackerName)}</Typography>
              {attackerStats.teamAssistance &&
                Object.entries(attackerStats.teamAssistance).map(
                  ([name, value]) => (
                    <TooltipDataItem
                      key={name}
                      title={formatName(name)}
                      value={value}
                    />
                  ),
                )}
              <CustomTooltip
                tooltipContent={
                  <AssistanceBonusTooltip
                    chainActionBonuses={attackerStats.chainActionBonuses}
                  />
                }>
                <TooltipDataItem
                  sx={{ borderTop: '1px solid black', color: '#FF3F84' }}
                  title={'Total'}
                  value={attackerStats.assistance}
                />
              </CustomTooltip>
            </Grid>
            <Grid paddingLeft={'40px'}>
              <Typography>{formatName(defenderName)}</Typography>
              {defenderStats.teamAssistance &&
                Object.entries(defenderStats.teamAssistance).map(
                  ([name, value]) => (
                    <TooltipDataItem
                      key={name}
                      title={formatName(name)}
                      value={value}
                    />
                  ),
                )}
              <CustomTooltip
                tooltipContent={
                  <AssistanceBonusTooltip
                    chainActionBonuses={defenderStats.chainActionBonuses}
                  />
                }>
                <TooltipDataItem
                  sx={{ borderTop: '1px solid black', color: '#FF3F84' }}
                  title={'Total'}
                  value={defenderStats.assistance}
                />
              </CustomTooltip>
            </Grid>
          </Grid>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default DoubleColAssistanceTooltip;
