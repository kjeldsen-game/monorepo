import { formatPlayerSkills } from "./ColumnUtils";

describe('formatPlayerSkills', () => {
  it('should return actual/potential when both values are present', () => {
    const skill = { PlayerSkills: { actual: 40, potential: 80 } };
    expect(formatPlayerSkills(skill)).toBe('40/80');
  });

  it('should default to 0 when actual or potential is missing', () => {
    expect(formatPlayerSkills({})).toBe('0/0');
    expect(formatPlayerSkills({ PlayerSkills: {} })).toBe('0/0');
    expect(formatPlayerSkills(null)).toBe('0/0');
  });

  it('should work with only one value provided', () => {
    expect(formatPlayerSkills({ PlayerSkills: { actual: 20 } })).toBe('20/0');
    expect(formatPlayerSkills({ PlayerSkills: { potential: 70 } })).toBe('0/70');
  });
});
