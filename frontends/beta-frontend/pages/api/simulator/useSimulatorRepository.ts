// import { connectorAPI } from '@/libs/fetcher';
// import useSWR, { mutate } from 'swr';

// const API = '/simulator/simulate-days';

// const fetcher = (token: string | null, data: any) => {
//   if (token === null) return undefined;
//   return connectorAPI<any>(`${API}`, 'POST', data, undefined, token);
// };

// const useSimulatorRepository = (token?: string) => {
//   const simulatePlayer = (data: any): Promise<any> => {
//     return connectorAPI<any>(API, 'POST', data, 'include', token)
//       .then((response) => {
//         mutate();
//         return response;
//       })
//       .catch((error) => {
//         console.error('Error updating auction:', error);
//         throw error;
//       });
//   };

//   return { simulatorResponse, isLoading, error };
// };

// export { useSimulatorRepository };
