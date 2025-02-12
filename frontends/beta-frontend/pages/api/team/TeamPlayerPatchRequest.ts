import { PlayerPosition } from '@/shared/models/PlayerPosition';
import { PlayerOrder } from '../match/models/MatchReportresponse';
import { TeamModifiers } from '@/shared/models/TeamModifiers';
import { PitchArea } from '@/shared/models/PitchArea';

interface PlayerRequest {
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
