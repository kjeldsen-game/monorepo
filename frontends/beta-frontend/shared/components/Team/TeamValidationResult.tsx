import { TeamFormationValiation } from '@/shared/models/Team';
import styled from '@emotion/styled';
import { Box, Tooltip, tooltipClasses, TooltipProps } from '@mui/material';
import React from 'react';
import GppMaybeIcon from '@mui/icons-material/GppMaybe';
import VerifiedUserIcon from '@mui/icons-material/VerifiedUser';
import MarketButton from '../Market/MarketButton';
import CustomTooltip from '../MatchReport/Tooltips/CustomTooltip';

interface TeamValidationResultProps {
  teamFormationValidation: TeamFormationValiation;
}

const TeamValidationResult: React.FC<TeamValidationResultProps> = ({
  teamFormationValidation,
}) => {
  console.log(teamFormationValidation);
  return (
    <CustomTooltip
      clickOpen={true}
      tooltipContent={
        <div>
          {teamFormationValidation.items.map((error, index) => (
            <div key={index}>- {error.message}</div>
          ))}
        </div>
      }>
      <MarketButton>
        {teamFormationValidation?.valid ? (
          <VerifiedUserIcon />
        ) : (
          <GppMaybeIcon />
        )}
      </MarketButton>
    </CustomTooltip>
  );
};

export default TeamValidationResult;
