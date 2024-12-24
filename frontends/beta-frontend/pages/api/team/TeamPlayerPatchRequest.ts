import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { PlayerOrder } from '../match/models/MatchReportresponse';
import { TeamModifiers } from '@/shared/models/TeamModifiers';

interface PlayerRequest {
  id: string;
  status: string;
  position: PlayerPosition;
  playerOrder: PlayerOrder;
}

export interface TeamPlayerPatchRequest {
  players: PlayerRequest[];
  teamModifiers: TeamModifiers;
}
