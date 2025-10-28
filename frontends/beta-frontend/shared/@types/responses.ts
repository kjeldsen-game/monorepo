export type NotificationResponse = {
    id: string;
    teamId: string;
    isRead: boolean;
    message: string;
    payload: Record<string, Object>
    type: NotificationType;
}

export enum NotificationType {
    AUCTION_BID = "AUCTION_BID",
    MATCH_END = "MATCH_END",
    LEAGUE_START = "LEAGUE_START"
}

