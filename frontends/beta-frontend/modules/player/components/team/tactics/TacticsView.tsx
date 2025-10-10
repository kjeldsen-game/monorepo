import { Box, Grid } from '@mui/material';
import React from 'react';
import {
    HorizontalPressure,
    Tactic,
    TeamModifiers,
    VerticalPressure,
} from '../../../types/TeamModifiers';
import { getModifierDescription } from '@/shared/utils/TeamModifiersUtils';
import PressureDescriptionItem from './PressureDescriptionItem';
import TeamModifiersForm from './TeamModifiersForm';
import ShieldIcon from '@mui/icons-material/Shield';
import { getModifierConfig } from 'modules/player/utils/TacticsUtils';

type TeamModifierChangeHandler = (
    value: Tactic | VerticalPressure | HorizontalPressure,
    type: string,
) => void;

interface TacticsViewProps {
    teamModifiers: TeamModifiers | undefined;
    handleTeamModifierChange: TeamModifierChangeHandler;
}

const TacticsView: React.FC<TacticsViewProps> = ({
    teamModifiers,
    handleTeamModifierChange,
}) => {

    return (
        <Box>
            <TeamModifiersForm
                teamModifiers={teamModifiers}
                handleModifierChange={handleTeamModifierChange}
            />
            {teamModifiers && (
                <Grid container spacing={1} paddingTop={1}>
                    {(Object.keys(teamModifiers)).reverse().map((key) => {
                        let modifierConfig = getModifierConfig(key);
                        return (
                            <Grid
                                key={key}
                                size={{ xs: 12, sm: 4 }}
                                sx={{ background: modifierConfig.light, borderRadius: 2 }}
                            >
                                <PressureDescriptionItem
                                    config={modifierConfig}
                                    description={getModifierDescription(teamModifiers[key])}
                                />
                            </Grid>
                        )
                    })}
                </Grid>
            )}
        </Box >
    );
};

export default TacticsView;
