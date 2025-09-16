import React from 'react';
import { render, screen } from '@testing-library/react';
import TrainingReportsTab from '../TrainingReportsTab';
import '@testing-library/jest-dom';

jest.mock('../../report/TrainingReport', () => ({
    __esModule: true,
    default: ({ date }: { date: string }) => (
        <div data-testid="training-report">Report for {date}</div>
    ),
}));

describe('TrainingReportsTab', () => {
    test('renders CircularProgress when trainings is undefined', () => {
        render(<TrainingReportsTab trainings={undefined as any} />);
        expect(screen.getByRole('progressbar')).toBeInTheDocument();
    });

    test('renders a TrainingReport component for each training date', () => {
        const mockTrainings = {
            '2025-08-29': [{ playerId: '1', skill: 'SCORING' }],
            '2025-08-30': [{ playerId: '2', skill: 'PASSING' }],
        } as any;

        render(<TrainingReportsTab trainings={mockTrainings} />);

        const reports = screen.getAllByTestId('training-report');
        expect(reports).toHaveLength(2);

        expect(screen.getByText('Report for 2025-08-29')).toBeInTheDocument();
        expect(screen.getByText('Report for 2025-08-30')).toBeInTheDocument();
    });
});
