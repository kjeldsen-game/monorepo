import { connectorAPI } from '@/libs/fetcher';
import { Pricing } from '@/shared/models/Economy';
import useSWR, { mutate } from 'swr';

const API = '/team/economy';

const fetcher = (token: string | null) => {
  if (token === null) {
    return undefined;
  }
  return connectorAPI<any>(API, 'GET', undefined, undefined, token);
};

const useEconomyRepository = (token?: string) => {
  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? API : null,
    () => fetcher(token ? token : null),
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
    return updateEconomy(API + '/sign-billboard', newData);
  };

  const signSponsor = (periodicity: string, mode: string): Promise<any> => {
    const newData = {
      periodicity: periodicity.toUpperCase(),
      mode: mode.toUpperCase(),
    };
    return updateEconomy(API + '/sign-sponsor', newData);
  };

  const editPricing = (data: Pricing[]): Promise<any> => {
    const newData = {
      prices: data,
    };
    return updateEconomy(API + '/update-pricing', newData);
  };

  return { data, isLoading, error, signBillboadDeal, signSponsor, editPricing };
};

export { useEconomyRepository };
