import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { PlayerOrder } from '../match/models/MatchReportresponse';

interface PlayerRequest {
  id: string;
  status: string;
  position: PlayerPosition;
  playerOrder: PlayerOrder;
}

export interface TeamPlayerPatchRequest {
  players: PlayerRequest[];
}
