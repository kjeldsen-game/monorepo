import { render, screen } from '@testing-library/react';
import ScheduleTrainingTab from '../ScheduleTrainingTab';
import '@testing-library/jest-dom';
import { SessionProvider } from 'next-auth/react';
import MockTrainingFilterProvider from 'modules/player/__mocks__/MockTrainingFilterProvider';

jest.mock('../../dialog/TrainingDialog', () => ({
    __esModule: true,
    default: (props: any) => {
        return (
            <div data-testid="training-dialog">
                Dialog Open: {props.open ? 'true' : 'false'}
                SkillToTrain: {props.skillToTrain}
                SkillUnderTraining: {props.skillUnderTraining}
                PlayerId: {props.playerId}
            </div>
        );
    },
}));

jest.mock('@/shared/components/Grid/Grid', () => ({
    __esModule: true,
    default: (props: any) => {
        return (
            <div data-testid="mock-grid">
                Mock Grid - Rows: {props.rows?.length}
            </div>
        );
    },
}));

describe('ScheduleTrainingTab', () => {
    const renderWithSession = (ui: React.ReactElement) => {
        const session = { user: { name: 'Test User', email: 'test@example.com' }, expires: '1', };
        return render(<SessionProvider session={session}>{ui}</SessionProvider>);
    };

    it('renders CircularProgress when no playersWithActiveTrainings is provided', () => {
        renderWithSession(
            <MockTrainingFilterProvider>
                <ScheduleTrainingTab playersWithActiveTrainings={undefined as any} />
            </MockTrainingFilterProvider>
        );
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });

    it('renders Grid and TrainingDialog when playersWithActiveTrainings is provided', () => {
        const mockPlayers = [
            {
                player: { id: 'player1', name: 'Player One' },
                skills: [],
            } as any,
        ];

        renderWithSession(<MockTrainingFilterProvider>
            <ScheduleTrainingTab playersWithActiveTrainings={mockPlayers as any} />
        </MockTrainingFilterProvider>);

        expect(screen.getByTestId('mock-grid')).toBeInTheDocument();
        expect(screen.getByText(/Rows: 1/)).toBeInTheDocument();
    });
});
