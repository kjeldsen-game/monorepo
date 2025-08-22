import { Box } from '@mui/material';
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
                    justifyContent: 'space-around',
                    height: '200px'
                }}>
                <TeamModifiersForm
                    teamModifiers={teamModifiers}
                    handleModifierChange={handleTeamModifierChange}
                />
                <Box
                    sx={{
                        width: '100%',
                        padding: '10px',
                        overflowY: 'auto',
                        height: '100%'
                    }}>
                    {teamModifiers && (
                        <Box
                            sx={{
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
                        </Box>
                    )}
                </Box>
            </Box >
        </>
    );
};

export default TacticsView;
