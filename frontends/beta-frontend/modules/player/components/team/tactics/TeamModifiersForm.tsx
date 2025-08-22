import { Box, useMediaQuery } from '@mui/material';
import React from 'react';
import { TeamModifiers, Tactic, VerticalPressure, HorizontalPressure } from '../../../types/TeamModifiers';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';
import { theme } from '@/libs/material/theme';

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
  const isXs = useMediaQuery(theme.breakpoints.down('sm'));

  return (
    <Box sx={{
      height: '100%',
      display: 'flex',
      flexDirection: 'column',
    }}>
      <CustomSelectInput
        sx={{ ...(isXs ? { width: '140px' } : {}) }}
        onChange={(e) =>
          handleModifierChange(e.target.value as Tactic, 'tactic')
        }
        value={teamModifiers?.tactic}
        values={Tactic}
        hideDefaultOption={true}
        title={'Tactic'}
      />
      <CustomSelectInput
        sx={{ ...(isXs ? { width: '140px' } : {}) }}
        onChange={(e) =>
          handleModifierChange(e.target.value as VerticalPressure, 'verticalPressure')
        }
        hideDefaultOption={true}
        value={teamModifiers?.verticalPressure}
        values={VerticalPressure}
        title={'Vertical Pressure'}
      />
      <CustomSelectInput
        sx={{ ...(isXs ? { width: '140px' } : {}) }}
        hideDefaultOption={true}
        onChange={(e) =>
          handleModifierChange(e.target.value as HorizontalPressure, 'horizontalPressure')
        }
        value={teamModifiers?.horizontalPressure}
        values={HorizontalPressure}
        title={'Horizontal Pressure'}
      />
    </Box>
  );
};

export default TeamModifiersForm;
