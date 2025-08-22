import {
  getEconomy,
  signBillboard,
  signSponsor,
  editPricing,
} from '../economyApi';
import { connectorAPI } from '@/libs/fetcher';

jest.mock('@/libs/fetcher', () => ({
  connectorAPI: jest.fn(),
}));

const mockedConnectorAPI = connectorAPI as jest.Mock;

describe('economyApi', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('calls connectorAPI correctly in getEconomy', () => {
    getEconomy('123', 'token123');
    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      '/team/123/economy',
      'GET',
      undefined,
      undefined,
      'token123',
    );
  });

  it('returns undefined if teamId or token is undefined in getEconomy', () => {
    expect(getEconomy(undefined as any, 'token')).toBeUndefined();
    expect(getEconomy('123', undefined as any)).toBeUndefined();
  });

  it('calls connectorAPI correctly in signBillboard', () => {
    const req = { contractId: 'abc' };
    signBillboard(req as any, '123', 'token123');
    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      '/team/123/economy/sign-billboard',
      'POST',
      req,
      'include',
      'token123',
    );
  });

  it('calls connectorAPI correctly in signSponsor', () => {
    const req = { sponsorId: 'xyz' };
    signSponsor(req as any, '123', 'token123');
    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      '/team/123/economy/sign-sponsor',
      'POST',
      req,
      'include',
      'token123',
    );
  });

  it('calls connectorAPI correctly in editPricing', () => {
    const req = { price: 100 };
    editPricing(req as any, '123', 'token123');
    expect(mockedConnectorAPI).toHaveBeenCalledWith(
      '/team/123/economy/update-pricing',
      'POST',
      req,
      'include',
      'token123',
    );
  });
});
