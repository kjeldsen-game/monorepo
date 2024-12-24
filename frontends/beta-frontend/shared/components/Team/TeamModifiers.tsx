import { Box, Typography } from '@mui/material';
import React from 'react';
import SelectInput from '../Common/SelectInput';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '@/shared/models/TeamModifiers';

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
  console.log(teamModifiers);
  console.log(teamModifiers?.tactic);
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minWidth: 120,
        marginLeft: '3rem',
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
              title={'Tactic'}
            />
            <SelectInput
              handleChange={(e) =>
                handleModifierChange(e.target.value, 'verticalPressure')
              }
              value={teamModifiers?.verticalPressure}
              values={VerticalPressure}
              title={'Vertical Pressure'}
            />
            <SelectInput
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
