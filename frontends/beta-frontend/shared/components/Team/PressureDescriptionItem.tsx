import { ModifierDescription } from '@/shared/models/TeamModifiers';
import { Typography } from '@mui/material';
import React from 'react';

interface PressureDescriptionProps {
  name: string;
  description?: ModifierDescription;
}

const PressureDescriptionItem: React.FC<PressureDescriptionProps> = ({
  description,
  name,
}) => {
  return (
    <>
      <>
        <Typography sx={{ fontWeight: 'bold' }}>{name}</Typography>
        {description?.effect && (
          <Typography>Effect: {description.effect}</Typography>
        )}
        {description?.purpose && (
          <Typography>Purpose: {description.purpose}</Typography>
        )}
        {description?.pros && <Typography>Pros: {description.pros}</Typography>}
        {description?.cons && <Typography>Cons: {description.cons}</Typography>}
      </>
    </>
  );
};

export default PressureDescriptionItem;
