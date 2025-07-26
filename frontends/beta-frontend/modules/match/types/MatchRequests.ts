import { MatchStatus } from '@/shared/models/match/Match';

export interface CreateMatchRequest {
  home: {
    id: string;
  };
  away: {
    id: string;
  };
}

export interface PatchMatchRequest {
  status: MatchStatus;
}
