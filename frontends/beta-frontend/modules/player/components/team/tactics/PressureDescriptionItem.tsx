import { ModifierDescription } from '@/shared/models/player/TeamModifiers';
import { convertSnakeCaseToTitleCase } from '@/shared/utils/StringUtils';
import { Box, Typography } from '@mui/material';
import { ModifierConfig } from 'modules/player/utils/TacticsUtils';
import React from 'react';

interface PressureDescriptionProps {
  description?: ModifierDescription;
  config: ModifierConfig
}

const PressureDescriptionItem: React.FC<PressureDescriptionProps> = ({
  description,
  config
}) => {

  return (
    <>
      <Box display={'flex'} padding={1}>
        <config.Icon sx={{ color: config.main }} />
        <Typography fontWeight={'bold'} paddingLeft={1}>
          {convertSnakeCaseToTitleCase(description?.name)}
        </Typography>
      </Box>
      <Box paddingX={2} paddingBottom={1} >
        {description?.effect && (
          <Typography fontSize={'14px'}>
            <Box component="span" fontWeight="bold">
              Effect:{' '}
            </Box>
            {description?.effect}
          </Typography>
        )}

        {description?.purpose && (
          <Typography fontSize={'14px'}>
            <Box component="span" fontWeight="bold">
              Purpose:{' '}
            </Box>
            {description?.purpose}
          </Typography>
        )}

        {description?.cons && (
          <Typography fontSize={'14px'}>
            <Box component="span" fontWeight="bold">
              Cons:{' '}
            </Box>
            {description?.cons}
          </Typography>
        )}

        {description?.pros && (
          <Typography fontSize={'14px'}>
            <Box component="span" fontWeight="bold">
              Pros:{' '}
            </Box>
            {description?.pros}
          </Typography>
        )}
      </Box>
    </>
  );
};

export default PressureDescriptionItem;
