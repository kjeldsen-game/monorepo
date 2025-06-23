import { PITCH_AREAS_ENUM } from '@/shared/models/match/PitchArea';
import {
  Category,
  Player,
  PlayerSkillRelevance,
} from '@/shared/models/player/Player';
import { PlayerLineupStatus } from '@/shared/models/player/PlayerLineupStatus';
import { PlayerOrder } from '@/shared/models/player/PlayerOrder';
import { PlayerPosition } from '@/shared/models/player/PlayerPosition';

export const mockPlayers: Player[] = [
  {
    id: '49d48f3f-046e-4c2d-a2de-96c03d723016',
    age: 30,
    name: 'Willy Treutel',
    position: undefined,
    teamRole: undefined,
    preferredPosition: PlayerPosition.FORWARD,
    playerOrder: PlayerOrder.NONE,
    playerOrderDestinationPitchArea: PITCH_AREAS_ENUM.CENTRE_BACK,
    status: PlayerLineupStatus.ACTIVE,
    category: Category.SENIOR,
    economy: {
      salary: 59550.0,
    },
    actualSkills: {
      TACKLING: {
        PlayerSkills: {
          actual: 75,
          potential: 75,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      DEFENSIVE_POSITIONING: {
        PlayerSkills: {
          actual: 75,
          potential: 75,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      CONSTITUTION: {
        PlayerSkills: {
          actual: 90,
          potential: 90,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      AERIAL: {
        PlayerSkills: {
          actual: 60,
          potential: 60,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      SCORING: {
        PlayerSkills: {
          actual: 20,
          potential: 20,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      BALL_CONTROL: {
        PlayerSkills: {
          actual: 20,
          potential: 20,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
    },
  },
  {
    id: '49d48f3f-046e-4c2d-a2de-96c01d723016',
    age: 30,
    name: 'Willy Two',
    position: PlayerPosition.CENTRE_BACK,
    teamRole: undefined,
    preferredPosition: PlayerPosition.FORWARD,
    playerOrder: PlayerOrder.NONE,
    playerOrderDestinationPitchArea: PITCH_AREAS_ENUM.CENTRE_BACK,
    status: PlayerLineupStatus.INACTIVE,
    category: Category.SENIOR,
    economy: {
      salary: 59550.0,
    },
    actualSkills: {
      TACKLING: {
        PlayerSkills: {
          actual: 75,
          potential: 75,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      DEFENSIVE_POSITIONING: {
        PlayerSkills: {
          actual: 75,
          potential: 75,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      CONSTITUTION: {
        PlayerSkills: {
          actual: 90,
          potential: 90,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      AERIAL: {
        PlayerSkills: {
          actual: 60,
          potential: 60,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      SCORING: {
        PlayerSkills: {
          actual: 20,
          potential: 20,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
      BALL_CONTROL: {
        PlayerSkills: {
          actual: 20,
          potential: 20,
          playerSkillRelevance: PlayerSkillRelevance.CORE,
        },
      },
    },
  },
];
