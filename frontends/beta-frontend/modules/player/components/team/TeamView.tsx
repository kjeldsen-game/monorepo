import { Team, TeamFormationValiation } from '@/shared/models/player/Team';
import { Box, Card, Tab, Typography, useMediaQuery } from '@mui/material';
import TeamDataGrid from './datagrids/TeamDatagrid';
import LineupFilters from './filters/LineupFilters';
import { useState } from 'react';
import { getFilteredLineupPlayers } from 'modules/player/utils/LineupUtils';
import CustomTabs from '@/shared/components/Tabs/CustomTabs';
import LineupView from './lineup/LineupView';
import LineupSpeedDial from './speedDial/LineupSpeedDial';
import TeamValidationDialog from './dialogs/TeamValidationDialog';
import { Player } from '@/shared/models/player/Player';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import { useLineupManager } from 'modules/player/hooks/logic/useLineupManager';
import { CustomTabPanel } from '@/shared/components/Tabs/CustomTabPanel';
import TacticsView from './tactics/TacticsView';
import { TeamPlayerPatchRequest } from 'modules/player/types/Requests';
import { useModalManager } from '@/shared/hooks/useModalManager';
import { theme } from '@/libs/material/theme';
import { useTabManager } from '@/shared/hooks/useTabManager';

interface TeamViewProps {
    team: Team | undefined;
    teamFormation: TeamFormationValiation | undefined;
    handleUpdateLineup?: (request: TeamPlayerPatchRequest) => void;
}

const TeamView: React.FC<TeamViewProps> = ({
    team,
    teamFormation,
    handleUpdateLineup = undefined,
}) => {
    const isXs = useMediaQuery(theme.breakpoints.down('sm'));
    const [filter, setFilter] = useState<string>('ALL');
    const [isXsPlayers, setIsXsPlayers] = useState<boolean>(false);
    const [edit, setEdit] = useState<boolean>(false);
    const [openModalValidation, setOpenModalValidation] =
        useState<boolean>(false);
    const { open, handleCloseModal, setOpen } = useModalManager();
    const { handleTabChange, selectedTab } = useTabManager()

    const {
        handleEdit,
        handleTeamModifierChange,
        players,
        teamModifiers,
        setActivePlayer,
        activePlayer,
    } = useLineupManager(team?.players, team?.teamModifiers, handleUpdateLineup);

    const handleSelectChange = (
        player: Player,
        value: PlayerOrder | any,
        field: string,
    ) => {
        const updatedPlayer: Player = { ...player, [field]: value };
        handleEdit(updatedPlayer, player);
    };

    const handleGridButtonClick = (player: Player) => {
        if (isXs) {
            setOpen(true);
        }
        setActivePlayer(activePlayer ? undefined : player);
    };

    return (
        <>
            <TeamValidationDialog
                teamValidation={teamFormation}
                open={openModalValidation}
                handleClose={() => setOpenModalValidation(false)}
            />
            <Card>
                <CustomTabs tabs={["Lineup", "Modifiers"]} selectedTab={selectedTab}
                    handleChange={handleTabChange} />
                <Box padding={1}>
                    <Box>
                        <CustomTabPanel value={selectedTab} index={0}>
                            <LineupView
                                handleCloseModal={handleCloseModal}
                                open={open}
                                edit={edit}
                                players={players}
                                handleEdit={handleEdit}
                                activePlayer={activePlayer}
                            />
                        </CustomTabPanel>
                        <CustomTabPanel value={selectedTab} index={1}>
                            <TacticsView
                                teamModifiers={teamModifiers}
                                handleTeamModifierChange={handleTeamModifierChange}
                            />
                        </CustomTabPanel>
                    </Box>
                </Box>
            </Card>

            <Box
                sx={{ width: '100%', background: 'white' }}
                padding={1}
                borderRadius={2}
                boxShadow={1}
                mt={2}>
                <Box>
                    <Typography fontWeight={'bold'} color={'#555F6C'}>
                        Team's Lineup
                    </Typography>
                </Box>
                <Box
                    display={'flex'}
                    justifyContent={'space-between'}
                    alignItems={'center'}>
                    <LineupFilters filter={filter} setFilter={setFilter} />
                    <Box position={'relative'} width={'50px'}>
                        <LineupSpeedDial
                            isEdit={edit}
                            isFormationValid={teamFormation?.valid}
                            isEditOnClick={() => setEdit(!edit)}
                            openModalValidationOnClick={() => setOpenModalValidation(true)}
                            toogleIsXsPlayers={() => setIsXsPlayers(!isXsPlayers)}
                        />
                    </Box>
                </Box>
                <TeamDataGrid
                    activePlayer={activePlayer}
                    onButtonClick={handleGridButtonClick}
                    isEditing={edit}
                    isXsPlayers={isXsPlayers}
                    rows={getFilteredLineupPlayers(players, filter)}
                    onSelectChange={handleSelectChange}
                />
            </Box>
        </>
    );
};
export default TeamView;
