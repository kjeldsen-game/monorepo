import { PlayerPosition } from '@/shared/models/PlayerPosition'

interface PlayerRequest {
  id: string
  status: string
  position: PlayerPosition
}

export interface TeamPlayerPatchRequest {
  players: PlayerRequest[]
}
