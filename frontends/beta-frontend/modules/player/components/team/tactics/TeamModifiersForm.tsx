import { Grid } from '@mui/material';
import React from 'react';
import { TeamModifiers, Tactic, VerticalPressure, HorizontalPressure } from '../../../types/TeamModifiers';
import { theme } from '@/libs/material/theme';
import CustomSelectInput from '@/shared/components/Common/CustomSelectInput';

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
    <Grid container spacing={1}>
      <Grid size={{ sm: 4, xs: 12 }}>
        <CustomSelectInput
          sx={{ width: 'auto' }}
          onChange={(e) =>
            handleModifierChange(e.target.value as HorizontalPressure, 'horizontalPressure')
          }
          value={teamModifiers?.horizontalPressure}
          values={HorizontalPressure}
          hideDefaultOption={true}
          title={'Horizontal Pressure'}
        />
      </Grid>

      <Grid size={{ sm: 4, xs: 12 }}>
        <CustomSelectInput
          sx={{ width: 'auto' }}
          onChange={(e) =>
            handleModifierChange(e.target.value as VerticalPressure, 'verticalPressure')
          }
          value={teamModifiers?.verticalPressure}
          values={VerticalPressure}
          hideDefaultOption={true}
          title={'Vertical Pressure'}
        />
      </Grid>
      <Grid size={{ sm: 4, xs: 12 }}>
        <CustomSelectInput
          sx={{ width: 'auto' }}
          onChange={(e) =>
            handleModifierChange(e.target.value as Tactic, 'tactic')
          }
          value={teamModifiers?.tactic}
          values={Tactic}
          hideDefaultOption={true}
          title={'Tactic'}
        />
      </Grid>
    </Grid>
  );
};

export default TeamModifiersForm;