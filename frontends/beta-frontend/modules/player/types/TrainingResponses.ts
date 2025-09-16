import { Player } from '@/shared/models/player/Player';
import { PlayerSkill } from '@/shared/models/player/PlayerSkill';

export type TeamTrainingEventsResponse = {
  trainings: Record<string, TrainingEventResponse[]>;
};

export type TrainingEventResponse = {
  player: Player;
  skill: PlayerSkill;
  points: number;
  pointsBeforeTraining: number;
  pointsAfterTraining: number;
  trainingType: TrainingType;
  trainingModifier: TrainingModifier;
  occuredAt: string;
  currentDay: number;
};

export enum TrainingType {
  PLAYER_TRAINING = 'PLAYER_TRAINING',
  DECLINE_TRAINING = 'DECLINE_TRAINING',
  POTENTIAL_RISE = 'POTENTIAL_RISE',
}

export enum TrainingModifier {
  FALL_OFF_CLIFF = 'FALL_OFF_CLIFF',
  BLOOM = 'BLOOM',
}

export interface PlayerScheduledTraningResponse {
  player: Player;
  skills: PlayerSkill[];
}
