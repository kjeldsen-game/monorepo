import { Player } from '@/shared/models/player/Player';

export interface AuctionResponse {
  id: string;
  teamId: string;
  averageBid: number;
  player: Partial<Player>;
  bidders: number;
  bid: number | null;
  endedAt: string;
  startedAt: string;
  status: MarketStatus;
}

export enum MarketStatus {
  COMPLETED = 'Completed',
  ACTIVE = 'Active',
  CANCEL = 'Cancel',
}
