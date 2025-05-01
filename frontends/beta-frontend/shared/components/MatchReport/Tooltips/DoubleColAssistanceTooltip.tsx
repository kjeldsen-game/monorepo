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
  const total =
    defenderStats.assistance?.adjusted != 0.0
      ? defenderStats.assistance?.adjusted
      : attackerStats.assistance?.adjusted;

  return (
    <CustomTooltip
      tooltipContent={
        <Box sx={{ color: 'black' }}>
          <Grid container>
            <Grid
              display={'flex'}
              flexDirection={'column'}
              justifyContent={'space-between'}>
              <Box>
                <Typography>{formatName(attackerName)}</Typography>
                {attackerStats.assistance?.teamAssistance &&
                  Object.entries(attackerStats.assistance?.teamAssistance).map(
                    ([name, value]) => (
                      <TooltipDataItem
                        key={name}
                        title={formatName(name)}
                        value={value}
                      />
                    ),
                  )}
                {attackerStats.assistance?.totalModifiers != 0 ? (
                  <CustomTooltip
                    tooltipContent={
                      <AssistanceBonusTooltip
                        chainActionBonuses={attackerStats.assistance?.modifiers}
                      />
                    }>
                    <TooltipDataItem
                      sx={{ color: '#FF3F84' }}
                      title={'Modifiers'}
                      value={attackerStats.assistance?.totalModifiers}
                    />
                  </CustomTooltip>
                ) : (
                  <TooltipDataItem
                    title={'Modifiers'}
                    value={attackerStats.assistance?.totalModifiers}
                  />
                )}
              </Box>
              <TooltipDataItem
                sx={{
                  borderTop: '1px solid black',
                  color: '#FF3F84',
                }}
                title={'Total'}
                value={attackerStats.assistance?.total}
              />
            </Grid>
            <Grid
              paddingLeft={'40px'}
              display={'flex'}
              flexDirection={'column'}
              justifyContent={'space-between'}>
              <Box>
                <Typography>{formatName(defenderName)}</Typography>
                {defenderStats.assistance?.teamAssistance &&
                  Object.entries(defenderStats.assistance?.teamAssistance).map(
                    ([name, value]) => (
                      <TooltipDataItem
                        key={name}
                        title={formatName(name)}
                        value={value}
                      />
                    ),
                  )}
                {defenderStats.assistance?.totalModifiers != 0 ? (
                  <CustomTooltip
                    tooltipContent={
                      <AssistanceBonusTooltip
                        chainActionBonuses={defenderStats.assistance?.modifiers}
                      />
                    }>
                    <TooltipDataItem
                      sx={{ color: '#FF3F84' }}
                      title={'Modifiers'}
                      value={defenderStats.assistance?.totalModifiers}
                    />
                  </CustomTooltip>
                ) : (
                  <TooltipDataItem
                    title={'Modifiers'}
                    value={defenderStats.assistance?.totalModifiers}
                  />
                )}
              </Box>
              <TooltipDataItem
                sx={{ borderTop: '1px solid black', color: '#FF3F84' }}
                title={'Total'}
                value={defenderStats.assistance?.total}
              />
            </Grid>
          </Grid>
          <Box display={'flex'} justifyContent={'center'}>
            <Box width={'50%'}>
              <CustomTooltip
                tooltipContent={
                  Math.abs(
                    defenderStats.assistance?.total -
                      attackerStats.assistance?.total,
                  ) +
                  ' = ' +
                  total
                }>
                <TooltipDataItem
                  sx={{
                    marginTop: '10px',
                    borderTop: '1px solid black',
                    color: '#FF3F84',
                    justifyContent: 'center',
                  }}
                  title={'Total'}
                  value={total}
                />
              </CustomTooltip>
            </Box>
          </Box>
        </Box>
      }>
      {children}
    </CustomTooltip>
  );
};

export default DoubleColAssistanceTooltip;
