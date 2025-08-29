import { render, screen } from '@testing-library/react';
import MatchReportStatsV2 from '../MatchReportStatsV2';
import { Stats } from 'modules/match/types/MatchResponses';

const homeStats: Stats = {
    goals: 2,
    shots: 10,
    missed: 3,
    saved: 5,
    passes: 25,
    missedPasses: 2,
    tackles: 8,
};

const awayStats: Stats = {
    goals: 1,
    shots: 8,
    missed: 2,
    saved: 4,
    passes: 20,
    missedPasses: 3,
    tackles: 6,
};

describe('MatchReportStatsV2', () => {
    it('renders all stat fields', () => {
        render(<MatchReportStatsV2 homeStats={homeStats} awayStats={awayStats} />);

        const statNames = [
            'Score',
            'Total Shots',
            'Missed Shots',
            'Saves',
            'Passes',
            'Missed Passes',
            'Tackles',
        ];

        statNames.forEach((name) => {
            expect(screen.getByText(name)).toBeInTheDocument();
        });
    });

    it('renders the Show Stats Table button', () => {
        render(<MatchReportStatsV2 homeStats={homeStats} awayStats={awayStats} />);

        const button = screen.getByRole('button', { name: /Show Stats Table/i });
        expect(button).toBeInTheDocument();
    });
});
