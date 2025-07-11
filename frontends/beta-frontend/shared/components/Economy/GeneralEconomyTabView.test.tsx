import { render, screen, fireEvent } from '@testing-library/react';
import GeneralEconomyTabView from './GeneralEconomyTabView';
import { IncomePeriodicity } from '@/shared/models/player/Economy';

jest.mock('./GeneralEconomyCard', () => () => <div>GeneralEconomyCard</div>);
jest.mock('./BillboardCard', () => (props: any) => (
    <div onClick={() => props.setOpen(true)}>BillboardCard</div>
));
jest.mock('./SponsorCard', () => (props: any) => (
    <div onClick={() => props.handleOpenModal(props.type)}>SponsorCard {props.type}</div>
));
jest.mock('./BillboardDialog', () => (props: any) =>
    props.open ? <div>BillboardDialog Open</div> : null
);
jest.mock('./SponsorDialog', () => (props: any) =>
    props.open ? <div>SponsorDialog Open for type {props.type}</div> : null
);

describe('GeneralEconomyTabView', () => {
    const mockSponsors = [
        { type: IncomePeriodicity.WEEKLY, name: 'Weekly Sponsor' },
        { type: IncomePeriodicity.ANNUAL, name: 'Annual Sponsor' },
    ];
    const mockBillboardDeal = { price: 1000 };
    const mockPrices = { billboard: 1000 };
    const mockBalance = 50000;

    it('renders all major sections', () => {
        render(
            <GeneralEconomyTabView
                sponsors={mockSponsors}
                billboardDeal={mockBillboardDeal}
                prices={mockPrices}
                balance={mockBalance}
            />
        );

        expect(screen.getByText('GeneralEconomyCard')).toBeInTheDocument();
        expect(screen.getByText('BillboardCard')).toBeInTheDocument();
        expect(screen.getByText(/SponsorCard Weekly/)).toBeInTheDocument();
        expect(screen.getByText(/SponsorCard Annual/)).toBeInTheDocument();
    });
});
