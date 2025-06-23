import {
  convertSnakeCaseToTitleCase,
  convertToKebabCase,
  toFirstUppercase,
} from './StringUtils';

describe('StringUtils', () => {
  it.each([
    ['test_word', 'Test Word'],
    ['test', 'Test'],
    [undefined, ''],
  ])(
    'should convert text from snake case to cammel case',
    (input: string | undefined, result: string) => {
      expect(convertSnakeCaseToTitleCase(input)).toStrictEqual(result);
    },
  );

  it.each([
    ['hello world', 'hello-world'],
    [undefined, ''],
  ])(
    'should convert text to kebab case',
    (input: string | undefined, result: string) => {
      expect(convertToKebabCase(input)).toStrictEqual(result);
    },
  );

  it.each([
    ['hello world', 'Hello world'],
    [undefined, ''],
  ])(
    'should convert text to kebab case',
    (input: string | undefined, result: string) => {
      expect(toFirstUppercase(input)).toStrictEqual(result);
    },
  );
});
