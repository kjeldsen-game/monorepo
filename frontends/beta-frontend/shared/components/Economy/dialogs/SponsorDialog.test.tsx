import { render, screen } from '@testing-library/react';
import { IncomePeriodicity } from '@/shared/models/player/Economy';
import '@testing-library/jest-dom';
import { useSession } from 'next-auth/react';
import SponsorDialog from './SponsorDialog';

jest.mock('@mui/icons-material/MonetizationOn', () => () => <div>MonetizationIcon</div>);

jest.mock('next-auth/react', () => ({
    useSession: jest.fn(),
}));

jest.mock('@/pages/api/economy/useEconomyRepository', () => ({
    useEconomyRepository: jest.fn(() => ({
        signSponsor: jest.fn(),
    })),
}));

jest.mock('@/shared/models/player/Economy', () => ({
    IncomePeriodicity: {
        WEEKLY: 'WEEKLY',
    },
    SPONSORS_OFFERS: {
        WEEKLY: {
            Conservative: { base: 100, bonus: 10 },
            Aggressive: { base: 200, bonus: 50 },
        },
    },
}));

describe('SponsorDialog', () => {
    beforeEach(() => {
        (useSession as jest.Mock).mockReturnValue({
            data: { user: { teamId: 'testTeam' }, accessToken: 'testToken' },
            status: 'authenticated',
        });
    });

    it.each([
        ['WEEKLY'],
        ['MONTHLY'],
        ['YEARLY'],
    ])('renders dialog title correctly for periodicity "%s"', (periodicity) => {
        render(
            <SponsorDialog
                open={true}
                handleClose={() => { }}
                type={periodicity as IncomePeriodicity}
            />
        );

        expect(
            screen.getByText(`Choose your new ${periodicity} Sponsor`)
        ).toBeInTheDocument();
    });
});
