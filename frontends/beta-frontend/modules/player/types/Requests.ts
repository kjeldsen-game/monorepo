import { IncomeMode, IncomePeriodicity } from '@/shared/models/player/Economy';
import { PriceItem } from './Economy';
import { DefaultResponse } from '@/shared/models/Responses';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import { TeamModifiers } from '@/shared/models/player/TeamModifiers';
import { PitchArea } from './Player';

export type EconomyRequestHandler<T> = (
  request: T,
  teamId: string,
  token: string,
) => Promise<DefaultResponse>;

export type EconomyEndpoint =
  | 'sign-billboard'
  | 'sign-sponsor'
  | 'update-pricing';

export type EconomyRequest =
  | SignBillboardRequest
  | SignSponsorRequest
  | PricingEditRequest;

export interface PricingEditRequest {
  prices: PriceItem[];
}

export interface SignSponsorRequest {
  periodicity: keyof typeof IncomePeriodicity;
  mode: keyof typeof IncomeMode;
}

export interface SignBillboardRequest {
  mode: keyof typeof IncomeMode;
}

export interface PlayerRequest {
  id: string;
  status: string;
  position: PlayerPosition;
  playerOrder: PlayerOrder;
  playerOrderDestinationPitchArea: PitchArea;
}

export interface TeamPlayerPatchRequest {
  players: PlayerRequest[];
  teamModifiers: TeamModifiers;
  self?: boolean;
}
