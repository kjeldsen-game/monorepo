import { INITIAL_MARKET_FILTER_FORM_VALUES } from '../hooks/useMarketFilterForm';
import { MarketFilterForm } from '../types/filterForm';
import { createAuctionQueryFilter } from './MarketUtils';

describe('createAuctionQueryFilter', () => {
  let formValues: MarketFilterForm;

  beforeEach(() => {
    formValues = INITIAL_MARKET_FILTER_FORM_VALUES;
  });

  it('creates query string with position only', () => {
    formValues.position = 'FORWARD';
    const query = createAuctionQueryFilter(formValues);
    expect(query).toBe('position=FORWARD');
  });

  it('formats potential skills with custom labels', () => {
    formValues.skillRanges.SC = {
      potentialMin: '5',
      potentialMax: '15',
      min: '0',
      max: '10',
    };

    const query = createAuctionQueryFilter(formValues);

    expect(query).toContain('skills=SCORING%3A0%3A10');
  });

  it('includes playerOffer minBid and maxBid', () => {
    formValues.playerOffer = { min: '1000', max: '5000' };
    const query = createAuctionQueryFilter(formValues);

    expect(query).toContain('minBid=1000');
    expect(query).toContain('maxBid=5000');
  });

  it('includes playerAge minAge and maxAge', () => {
    formValues.playerAge = { min: '18', max: '25' };
    const query = createAuctionQueryFilter(formValues);
    expect(query).toContain('minAge=18');
    expect(query).toContain('maxAge=25');
  });

  it('combines all filters correctly', () => {
    formValues.playerAge = { min: '18', max: '25' };
    formValues.playerOffer = { min: '1000', max: '5000' };

    const query = createAuctionQueryFilter(formValues);

    expect(query).toContain('position=FORWARD');
    expect(query).toContain('skills=SCORING');

    expect(query).toContain('minBid=1000');
    expect(query).toContain('maxBid=5000');
    expect(query).toContain('minAge=18');
    expect(query).toContain('maxAge=25');
  });
});
