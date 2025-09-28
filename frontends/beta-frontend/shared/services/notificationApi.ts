import { connectorAPI } from '@/libs/fetcher';

export const NOTIFICATIONS_API = '/notifications';

export const getNotifications = (token: string | null, teamId?: string) => {
  if (token === null) return undefined;
  return connectorAPI<any>(
    `${NOTIFICATIONS_API}?teamId=${teamId}`,
    'GET',
    undefined,
    undefined,
    token,
  );
};

export const markAsReadNotification = (token: string | null, notificationId?: string) => {
if (token === null) return undefined;
  return connectorAPI<any>(
    `${NOTIFICATIONS_API}/${notificationId}`,
    'PUT',
    undefined,
    undefined,
    token,
  );
}