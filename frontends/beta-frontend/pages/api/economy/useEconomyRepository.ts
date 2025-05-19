import { connectorAPI } from '@/libs/fetcher';
import { Pricing } from '@/shared/models/player/Economy';
import useSWR, { mutate } from 'swr';

const API = '/team/';

const fetcher = (teamId: string | null, token: string | null) => {
  if (token === null || teamId === null) {
    return undefined;
  }
  return connectorAPI<any>(
    API + teamId + '/economy',
    'GET',
    undefined,
    undefined,
    token,
  );
};

const useEconomyRepository = (teamId?: string, token?: string) => {
  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? API : null,
    () => fetcher(teamId ? teamId : null, token ? token : null),
  );

  const updateEconomy = (url: string, data: any): Promise<any> => {
    return connectorAPI<any>(url, 'POST', data, 'include', token)
      .then((response) => {
        mutate();
        return response;
      })
      .catch((error) => {
        console.error('Error updating auction:', error);
        throw error;
      });
  };

  const signBillboadDeal = (value: string): Promise<any> => {
    const newData = {
      mode: value.toUpperCase(),
    };
    return updateEconomy(API + teamId + '/economy/sign-billboard', newData);
  };

  const signSponsor = (periodicity: string, mode: string): Promise<any> => {
    const newData = {
      periodicity: periodicity.toUpperCase(),
      mode: mode.toUpperCase(),
    };
    return updateEconomy(API + teamId + '/economy/sign-sponsor', newData);
  };

  const editPricing = (data: Pricing[]): Promise<any> => {
    const newData = {
      prices: data,
    };
    return updateEconomy(API + teamId + '/economy/update-pricing', newData);
  };

  return { data, isLoading, error, signBillboadDeal, signSponsor, editPricing };
};

export { useEconomyRepository };
