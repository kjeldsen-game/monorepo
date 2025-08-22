import { render, screen, fireEvent, within } from '@testing-library/react';
import SponsorCard from '../SponsorCard';
import { IncomeMode, IncomePeriodicity } from '@/shared/models/player/Economy';

describe('SponsorCard', () => {

    afterEach(() => {
        jest.clearAllMocks();
    });

    const mockHandleOpenModal = jest.fn();
    const defaultProps = {
        open: true,
        handleOpenModal: mockHandleOpenModal,
    };

    it.each([
        ['WEEKLY'],
        ['ANNUAL'],
    ])('renders the sponsor type title for type %s', (type) => {
        render(
            <SponsorCard
                open={false}
                mode={null}
                handleOpenModal={jest.fn()}
                type={type as any}
            />
        );
        expect(screen.getByText(new RegExp(`${type} Sponsor`, 'i'))).toBeInTheDocument();
    });

    it.each([
        ['WEEKLY'],
        ['ANNUAL'],
    ])('calls handleOpenModal with correct type and shows fallback text for type %s when mode is null', (type) => {
        const mockHandleOpenModal = jest.fn();

        render(
            <SponsorCard
                open={false}
                mode={null}
                handleOpenModal={mockHandleOpenModal}
                type={type as any}
            />
        );

        const button = screen.getByRole('button', { name: /choose offer/i });
        expect(button).toBeInTheDocument();
        const fallbackText = screen.getByText((content) =>
            content.toLowerCase().includes("you don't have selected")
        );
        expect(fallbackText).toBeInTheDocument();
        fireEvent.click(button);
        expect(mockHandleOpenModal).toHaveBeenCalledWith(type);
    });

    it.each([
        [IncomeMode.AGGRESSIVE, IncomePeriodicity.ANNUAL],
        [IncomeMode.CONSERVATIVE, IncomePeriodicity.ANNUAL],
        [IncomeMode.MODERATE, IncomePeriodicity.ANNUAL],
        [IncomeMode.AGGRESSIVE, IncomePeriodicity.WEEKLY],
        [IncomeMode.CONSERVATIVE, IncomePeriodicity.WEEKLY],
        [IncomeMode.MODERATE, IncomePeriodicity.WEEKLY],
    ] as [IncomeMode, IncomePeriodicity][])(
        'renders type "%s" and mode "%s" correctly',
        (mode, type) => {
            render(
                <SponsorCard
                    {...defaultProps}
                    mode={mode}
                    type={type}
                />
            );

            expect(screen.getByText(new RegExp(`${type} Sponsor`, 'i'))).toBeInTheDocument();

            const card = screen.getByTestId(`sponsor-card-${type.toLowerCase()}`);

            expect(within(card).getByText('Mode:')).toBeInTheDocument();
            expect(within(card).getByText(mode)).toBeInTheDocument();
            expect(
                screen.queryByRole('button', { name: /choose offer/i })
            ).not.toBeInTheDocument();
        }
    );
});
