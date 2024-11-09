import { connectorAPI } from '@/libs/fetcher';
import { Pricing } from '@/shared/models/Economy';
import useSWR from 'swr';

const useEconomyRepository = (teamId?: string) => {
    const { data, mutate } = useSWR<any>(
        '/team/' + teamId + '/economy',
        connectorAPI,
    );

    const signBillboadDeal = (value: string): Promise<any> => {
        const newData = {
            mode: value.toUpperCase(),
        };

        return connectorAPI<any>(
            '/simulator/billboards-selection/' + teamId,
            'POST',
            newData,
            'include',
        )
            .then((response) => {
                mutate();
                return response;
            })
            .catch((error) => {
                console.error('Error updating auction:', error);
                throw error;
            });
    };

    const signSponsor = (periodicity: string, mode: string): Promise<any> => {
        const newData = {
            periodicity: periodicity.toUpperCase(),
            mode: mode.toUpperCase(),
        };

        return connectorAPI<any>(
            '/simulator/sign-sponsor/' + teamId,
            'POST',
            newData,
            'include',
        )
            .then((response) => {
                mutate();
                return response;
            })
            .catch((error) => {
                console.error('Error updating auction:', error);
                throw error;
            });
    };

    const editPricing = (data: Pricing[]): Promise<any> => {
        const newData = {
            prices: data,
        };
        return connectorAPI<any>(
            '/simulator/update-pricing/' + teamId,
            'POST',
            newData,
            'include',
        )
            .then((response) => {
                mutate();
                return response;
            })
            .catch((error) => {
                console.error('Error updating auction:', error);
                throw error;
            });
    };

    return { data, signBillboadDeal, signSponsor, editPricing };
};

export { useEconomyRepository };
