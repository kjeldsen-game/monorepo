import useSWR from 'swr';
import { useSession } from 'next-auth/react';
import * as notificationApi from '../services/notificationApi';
import { useSnackbar } from 'notistack';
import { NotificationResponse } from '../@types/responses';

export const useNotificationApi = () => {
  const { data: userData } = useSession();

    const { data, error, isLoading, mutate } = useSWR<NotificationResponse[]>(
    userData?.accessToken ? notificationApi.NOTIFICATIONS_API : null,
    () => notificationApi.getNotifications(userData.accessToken!, userData?.user?.teamId),
    {
        refreshInterval: 10000, 
    }
    );

    const handleNotificationRead = async (
      notificationId: string,
    ): Promise<void> => {
        console.log(notificationId)
      try {
        await notificationApi.markAsReadNotification(
          userData.accessToken!, notificationId
        );
        mutate();
      } catch (err: any) {
        console.log()
      }
    };

  return {
    handleNotificationRead,
    data,
    error,
    isLoading,
  };
};
