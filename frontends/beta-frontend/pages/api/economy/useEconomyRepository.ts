import { connectorAPI } from '@/libs/fetcher';
import { useError } from '@/shared/contexts/ErrorContext';
import { useNotification } from '@/shared/contexts/NotificationContext';
import { Pricing } from '@/shared/models/player/Economy';
import { PricingEditRequest } from 'modules/player/types/Requests';
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
  const { setError } = useError();
  const { setNotification } = useNotification();

  const { data, error, isLoading, mutate } = useSWR<any>(
    token ? API : null,
    () => fetcher(teamId ? teamId : null, token ? token : null),
  );

  const updateEconomy = (
    url: string,
    data: any,
    message: string,
  ): Promise<any> => {
    return connectorAPI<any>(url, 'POST', data, 'include', token)
      .then((response) => {
        mutate();
        setNotification(message);
        return response;
      })
      .catch((error) => {
        setError(error.message);
      });
  };

  const signBillboadDeal = (value: string): Promise<any> => {
    const newData = {
      mode: value.toUpperCase(),
    };
    return updateEconomy(
      API + teamId + '/economy/sign-billboard',
      newData,
      'Billboard was successfuly signed!',
    );
  };

  const signSponsor = (periodicity: string, mode: string): Promise<any> => {
    const newData = {
      periodicity: periodicity.toUpperCase(),
      mode: mode.toUpperCase(),
    };
    return updateEconomy(
      API + teamId + '/economy/sign-sponsor',
      newData,
      'Sponsor was successfuly signed!',
    );
  };

  const editPricing = (data: Pricing[]): Promise<any> => {
    const newData = {
      prices: data,
    };
    return updateEconomy(
      API + teamId + '/economy/update-pricing',
      newData,
      'Team pricing was successfuly updated!',
    );
  };

  return { data, isLoading, error, signBillboadDeal, signSponsor, editPricing };
};

export { useEconomyRepository };
