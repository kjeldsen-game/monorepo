import { render, screen } from '@testing-library/react';
import TeamView from './TeamView';
import { Team, TeamFormationValiation } from '@/shared/models/player/Team';
import { Player } from '@/shared/models/player/Player';
import { SessionProvider } from 'next-auth/react';

jest.mock('./lineup/LineupView', () => ({
    __esModule: true,
    default: () => <div data-testid="LineupView" />
}));
jest.mock('./tactics/TacticsView', () => ({
    __esModule: true,
    default: () => <div data-testid="TacticsView" />
}));
jest.mock('./datagrids/TeamDatagrid', () => ({
    __esModule: true,
    default: () => <div data-testid="TeamDataGrid" />
}));
jest.mock('./filters/LineupFilters', () => ({
    __esModule: true,
    default: () => <div data-testid="LineupFilters" />
}));
jest.mock('./speedDial/LineupSpeedDial', () => ({
    __esModule: true,
    default: () => <div data-testid="LineupSpeedDial" />
}));
jest.mock('./dialogs/TeamValidationDialog', () => ({
    __esModule: true,
    default: () => <div data-testid="TeamValidationDialog" />
}));
jest.mock('@/shared/components/Tabs/CustomTabs', () => ({
    __esModule: true,
    default: ({ children }: any) => <div data-testid="CustomTabs">{children}</div>
}));
jest.mock('@/shared/components/Tabs/CustomTabPanel', () => ({
    __esModule: true,
    CustomTabPanel: ({ children }: any) => <div data-testid="CustomTabPanel">{children}</div>
}));

describe('TeamView', () => {
    const mockTeam: Team = {
        id: '1',
        name: 'Mock Team',
        players: [{ name: 'Player1' }, { name: 'Player2' }] as Player[],
        teamModifiers: {}
    };
    const mockFormation: TeamFormationValiation = {
        valid: true,
        items: []
    };

    it('renders all child components', () => {
        render(
            <SessionProvider session={null}>
                <TeamView team={mockTeam} teamFormation={mockFormation} />
            </SessionProvider>);

        expect(screen.getByTestId('LineupView')).toBeInTheDocument();
        expect(screen.getByTestId('TacticsView')).toBeInTheDocument();
        expect(screen.getByTestId('TeamDataGrid')).toBeInTheDocument();
        expect(screen.getByTestId('LineupFilters')).toBeInTheDocument();
        expect(screen.getByTestId('LineupSpeedDial')).toBeInTheDocument();
        expect(screen.getByTestId('TeamValidationDialog')).toBeInTheDocument();
        expect(screen.getByTestId('CustomTabs')).toBeInTheDocument();
        expect(screen.getAllByTestId('CustomTabPanel').length).toBe(2); // Lineup + Tactics
    });
});
