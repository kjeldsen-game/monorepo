export interface AuctionDetail {
  id: string
  averageBid: number
  bids: Bid[]
  endedAt: string
  playerId: string
  startAt: string
  status: string
  teamId: string
}

export interface Bid {
    amount: number
    teamId: string
    timestamp: string
}