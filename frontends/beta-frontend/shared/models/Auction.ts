import { Player } from './Player';

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
