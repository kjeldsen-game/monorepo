import { getBackgroundColor, isNegative } from './EconomyUtils';

describe('EconomyUtils', () => {
  it.each([
    [100, false],
    [-100, true],
  ])(
    'should test if the value is negative or not',
    (input: number, result: boolean) => {
      expect(isNegative(input)).toBe(result);
    },
  );

  it.each([
    ['Total Income', '#A4BC100D'],
    ['Total Outcome', '#C51A1A0D'],
    ['Total Balance', '#F5F5F5'],
    ['Default text', 'transparent'],
  ])(
    'should return the correct background color based on the column name',
    (input: string, resultColor: string) => {
      expect(getBackgroundColor(input)).toStrictEqual({
        backgroundColor: resultColor,
      });
    },
  );
});
