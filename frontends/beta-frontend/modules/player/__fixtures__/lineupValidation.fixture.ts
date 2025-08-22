import { LineupValidationResponse } from '../types/Responses';

export const lineupValidationFalseMock: LineupValidationResponse = {
  valid: false,
  items: [
    { message: 'Player 1 is valid', valid: true },
    { message: 'Player 2 is invalid', valid: false },
  ],
};

export const lineupValidationTrueMock: LineupValidationResponse = {
  valid: true,
  items: [
    { message: 'Player 1 is valid', valid: true },
    { message: 'Player 2 is valid', valid: true },
  ],
};
