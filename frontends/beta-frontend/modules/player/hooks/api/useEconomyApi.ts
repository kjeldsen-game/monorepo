import { useSession } from 'next-auth/react';
import useSWR from 'swr';
import { TEAMS_API } from '../../services/teamApi';
import { getEconomy } from '../../services/economyApi';
import * as economyApi from '../../services/economyApi';
import {
  EconomyRequestHandler,
  PricingEditRequest,
  SignBillboardRequest,
  SignSponsorRequest,
} from '../../types/Requests';
import { EconomyResponse } from '../../types/Responses';
import { useSnackbar } from 'notistack';

export const useEconomyApi = () => {
  const { data: userData } = useSession();
  const { enqueueSnackbar } = useSnackbar();

  const { data, error, isLoading, mutate } = useSWR<EconomyResponse>(
    userData?.accessToken ? TEAMS_API + userData?.user?.teamId : null,
    () => getEconomy(userData?.user.teamId, userData?.accessToken),
  );

  const handleEconomyRequest = async <T>(
    request: T,
    apiFn: EconomyRequestHandler<T>,
    errorMessage: string,
  ): Promise<void> => {
    try {
      const response = await apiFn(
        request,
        userData?.user.teamId,
        userData?.accessToken,
      );
      enqueueSnackbar(response.message, { variant: 'success' });
      mutate();
    } catch (err: any) {
      enqueueSnackbar(err.message || errorMessage, { variant: 'error' });
    }
  };

  const handleBillboardSign = (
    request: SignBillboardRequest,
    closeFn: () => void,
  ) => {
    handleEconomyRequest(
      request,
      economyApi.signBillboard,
      'Billboard sign failed',
    );
    closeFn();
  };

  const handleEditPricing = (request: PricingEditRequest) =>
    handleEconomyRequest(
      request,
      economyApi.editPricing,
      'Edit pricing failed',
    );

  const handleSponsorSign = (
    request: SignSponsorRequest,
    closeFn: () => void,
  ) => {
    handleEconomyRequest(
      request,
      economyApi.signSponsor,
      'Sponsor sign failed',
    );
    closeFn();
  };

  return {
    handleSponsorSign,
    handleEditPricing,
    handleBillboardSign,
    data,
    error,
    isLoading,
  };
};
