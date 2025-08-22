import { connectorAPI } from '@/libs/fetcher';
import { TEAMS_API } from './teamApi';
import { DefaultResponse } from '@/shared/models/Responses';
import {
  EconomyEndpoint,
  EconomyRequest,
  PricingEditRequest,
  SignBillboardRequest,
  SignSponsorRequest,
} from '../types/Requests';

export const getEconomy = (teamId: string | null, token: string | null) => {
  if (token === undefined || teamId === undefined) {
    return undefined;
  }
  return connectorAPI<any>(
    `${TEAMS_API}/${teamId}/economy`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

const updateEconomy = <T extends EconomyRequest>(
  endpoint: EconomyEndpoint,
  teamId: string,
  data: T,
  token: string,
): Promise<DefaultResponse> => {
  const url = `${TEAMS_API}/${teamId}/economy/${endpoint}`;
  return connectorAPI<T>(url, 'POST', data, 'include', token);
};

export const signBillboard = (
  request: SignBillboardRequest,
  teamId: string,
  token: string,
) => updateEconomy('sign-billboard', teamId, request, token);

export const signSponsor = (
  request: SignSponsorRequest,
  teamId: string,
  token: string,
) => updateEconomy('sign-sponsor', teamId, request, token);

export const editPricing = (
  request: PricingEditRequest,
  teamId: string,
  token: string,
) => updateEconomy('update-pricing', teamId, request, token);
