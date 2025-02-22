import { Box, Typography } from '@mui/material';
import React from 'react';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '@/shared/models/TeamModifiers';
import SelectInput from '../../Common/SelectInput';

interface TeamModifiersFormProps {
  teamModifiers: TeamModifiers | undefined;
  handleModifierChange: (
    value: Tactic | VerticalPressure | HorizontalPressure,
    type: string,
  ) => void;
}

const TeamModifiersForm: React.FC<TeamModifiersFormProps> = ({
  teamModifiers,
  handleModifierChange,
}) => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minWidth: 120,
        // marginLeft: '1rem',
        padding: '10px',
      }}>
      <Typography align="center" variant="h6">
        Team Modifiers
      </Typography>
      <Box>
        {teamModifiers && (
          <>
            <SelectInput
              handleChange={(e) =>
                handleModifierChange(e.target.value, 'tactic')
              }
              value={teamModifiers?.tactic}
              values={Tactic}
              hideDefaultOption={true}
              title={'Tactic'}
            />
            <SelectInput
              handleChange={(e) =>
                handleModifierChange(e.target.value, 'verticalPressure')
              }
              hideDefaultOption={true}
              value={teamModifiers?.verticalPressure}
              values={VerticalPressure}
              title={'Vertical Pressure'}
            />
            <SelectInput
              hideDefaultOption={true}
              handleChange={(e) =>
                handleModifierChange(e.target.value, 'horizontalPressure')
              }
              value={teamModifiers?.horizontalPressure}
              values={HorizontalPressure}
              title={'Horizontal Pressure'}
            />
          </>
        )}
      </Box>
    </Box>
  );
};

export default TeamModifiersForm;
