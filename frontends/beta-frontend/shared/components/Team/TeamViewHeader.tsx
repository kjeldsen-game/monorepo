import { Box, Collapse, Typography } from '@mui/material';
import React from 'react';
import TeamDetails from '../TeamDetails';
import TeamModifiersForm from './TeamModifiers';
import DoneIcon from '@mui/icons-material/Done';
import CloseIcon from '@mui/icons-material/Close';
import {
  HorizontalPressure,
  Tactic,
  TeamModifiers,
  VerticalPressure,
} from '@/shared/models/TeamModifiers';

type TeamModifierChangeHandler = (
  value: Tactic | VerticalPressure | HorizontalPressure,
  type: string,
) => void;

interface TeamViewHeaderProps {
  name: string | undefined;
  isEditing: boolean;
  teamModifiers: TeamModifiers | undefined;
  handleTeamModifierChange: TeamModifierChangeHandler;
  showValidation: boolean;
  teamFormationValidation: any;
}

const TeamViewHeader: React.FC<TeamViewHeaderProps> = ({
  name,
  isEditing,
  teamModifiers,
  handleTeamModifierChange,
  showValidation,
  teamFormationValidation,
}) => {
  return (
    <>
      <Box
        sx={{
          width: '50%',
          display: 'flex',
          alignItems: 'center',
          height: '100%',
          padding: '10px',
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
          padding: '10px',
          width: '50%',
          height: 200,
          overflow: 'scroll',
        }}>
        <Collapse in={showValidation} timeout="auto">
          <div
            style={{
              borderRadius: '8px',
              padding: '10px',
              maxHeight: '100%',
              overflowY: 'auto',
              background: 'white',
            }}>
            {teamFormationValidation?.items?.map((error, index) => (
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  width: '100%',
                }}
                key={index}>
                <Typography>{error.message}</Typography>
                <Box
                  sx={{
                    color: error.valid ? 'green' : 'red',
                  }}>
                  {error.valid ? <DoneIcon /> : <CloseIcon />}
                </Box>
              </Box>
            ))}
          </div>
        </Collapse>
      </Box>
    </>
  );
};

export default TeamViewHeader;
