import {
  HorizontalPressure,
  VerticalPressure,
} from '../models/player/TeamModifiers';
import { getModifierDescription } from './TeamModifiersUtils';

describe('TeamModifiersUtils', () => {
  it.each([
    VerticalPressure.HIGH_PRESSURE,
    VerticalPressure.MID_PRESSURE,
    VerticalPressure.LOW_PRESSURE,
    VerticalPressure.NO_VERTICAL_FOCUS,
    HorizontalPressure.NO_HORIZONTAL_FOCUS,
    HorizontalPressure.SWARM_CENTRE,
    HorizontalPressure.SWARM_FLANKS,
    undefined,
  ])(
    'should return modifier description for vertical pressure input: %s',
    (input: VerticalPressure | HorizontalPressure | undefined) => {
      const result = getModifierDescription(input);

      if (input === undefined) {
        expect(result).toBeUndefined();
      } else {
        expect(result).toBeDefined();
        expect(result?.name).toEqual(input);
        expect(result?.pros).toBeDefined;
        expect(result?.cons).toBeDefined;
        expect(result?.effect).toBeUndefined;
        expect(result?.purpose).toBeUndefined;
      }
    },
  );
});
