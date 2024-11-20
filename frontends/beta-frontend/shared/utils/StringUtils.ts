export function convertSnakeCaseToTitleCase(input: string | undefined) {
  if (input === undefined) {
    return '';
  }
  return input
    .toLowerCase()
    .split('_')
    .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
}

export const convertToKebabCase = (str: string) => {
  return str
    .toLowerCase()
    .replace(/\s+/g, '-')
    .replace(/[^\w-]+/g, '');
};
