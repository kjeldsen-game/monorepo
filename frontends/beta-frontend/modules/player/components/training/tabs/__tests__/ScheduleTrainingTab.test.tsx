import { render, screen } from '@testing-library/react';
import ScheduleTrainingTab from '../ScheduleTrainingTab';
import '@testing-library/jest-dom';

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
    test('renders CircularProgress when no playersWithActiveTrainings is provided', () => {
        render(<ScheduleTrainingTab playersWithActiveTrainings={undefined as any} />);
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });

    test('renders Grid and TrainingDialog when playersWithActiveTrainings is provided', () => {
        const mockPlayers = [
            {
                player: {
                    id: 'player1',
                    name: 'Player One',
                },
                skills: [],
            } as any,
        ];

        render(<ScheduleTrainingTab playersWithActiveTrainings={mockPlayers} />);

        expect(screen.getByTestId('mock-grid')).toBeInTheDocument();
        expect(screen.getByText(/Rows: 1/)).toBeInTheDocument();

        expect(screen.getByTestId('training-dialog')).toBeInTheDocument();
    });
});
