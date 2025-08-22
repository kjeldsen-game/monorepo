import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { PlayerOrder } from '../match/models/MatchReportresponse';
import { TeamModifiers } from '@/shared/models/player/TeamModifiers';
import { PitchArea } from '@/shared/models/match/PitchArea';

export interface PlayerRequest {
  id: string;
  status: string;
  position: PlayerPosition;
  playerOrder: PlayerOrder;
  playerOrderSpecification: any | PitchArea;
}

export interface TeamPlayerPatchRequest {
  players: PlayerRequest[];
  teamModifiers: TeamModifiers;
}
