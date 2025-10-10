import { formatName, getSurname } from './PlayerUtils';

describe('PlayerUtils', () => {
  it.each([
    ['Test name of the player', 'player'],
    ['', ''],
  ])(
    'should return last name of the player names',
    (inputName: string, resultString: string) => {
      expect(getSurname(inputName)).toBe(resultString);
    },
  );

  it.each([
    ['Test name of the player', 'T. player'],
    ['', ''],
  ])(
    'should format name to contain first letter of firstname and last 2 surnames',
    (inputName: string, resultString: string) => {
      expect(formatName(inputName)).toBe(resultString);
    },
  );
});
