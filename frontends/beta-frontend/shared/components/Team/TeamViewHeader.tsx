import { Box, Collapse, Typography } from '@mui/material';
import React, { useState } from 'react';
import TeamDetails from '../TeamDetails';
import TeamModifiersForm from './TeamModifiers';

import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '@/shared/models/TeamModifiers';
import { getModifierDescription } from '@/shared/utils/TeamModifiersUtils';
import PressureDescriptionItem from './PressureDescriptionItem';

type TeamModifierChangeHandler = (
  value: Tactic | VerticalPressure | HorizontalPressure,
  type: string,
) => void;

interface TeamViewHeaderProps {
  name: string | undefined;
  isEditing: boolean;
  teamModifiers: TeamModifiers | undefined;
  handleTeamModifierChange: TeamModifierChangeHandler;
}

const TeamViewHeader: React.FC<TeamViewHeaderProps> = ({
  name,
  isEditing,
  teamModifiers,
  handleTeamModifierChange,
}) => {
  const tacticDescription = getModifierDescription(teamModifiers?.tactic);
  const horizontalPressureDescription = getModifierDescription(
    teamModifiers?.horizontalPressure,
  );
  const verticalPressureDescription = getModifierDescription(
    teamModifiers?.verticalPressure,
  );

  return (
    <>
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          height: '100%',
          padding: '10px',
          margin: '20px',
          width: '30%',
        }}>
        <TeamDetails name={name} />
        {/* <PlayerTactics /> */}
        {isEditing && (
          <TeamModifiersForm
            teamModifiers={teamModifiers}
            handleModifierChange={handleTeamModifierChange}
          />
        )}
      </Box>
      <Box
        sx={{
          width: '70%',
          height: '250px',
          padding: '10px',
          overflow: 'scroll',
        }}>
        <div
          style={{
            borderRadius: '8px',
            padding: '10px',
            maxHeight: '100%',
            overflowY: 'auto',
            background: 'white',
          }}>
          <Box
            sx={{
              justifyContent: 'space-between',
            }}>
            <PressureDescriptionItem
              name={'Tactic'}
              description={tacticDescription}
            />

            <PressureDescriptionItem
              name={'Horizontal Pressure'}
              description={horizontalPressureDescription}
            />
            <PressureDescriptionItem
              name={'Vertical Pressure'}
              description={verticalPressureDescription}
            />
          </Box>
        </div>
      </Box>
    </>
  );
};

export default TeamViewHeader;
