import { Player } from './Player';

export interface AuctionMarket {
  id: string;
  teamId: string;
  averageBid: number;
  player: Player;
  bidders: number;
  bid: number | null;
  endedAt: string;
}

export interface Auction {
  id: string;
  averageBid: number;
  player: Player;
  bids: number;
}

export interface PlayerAuction {
  id: string;
  averageBid: number;
  player: Player;
}
