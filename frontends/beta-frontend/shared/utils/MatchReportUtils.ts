export function formatName(name?: string): string {
  if (!name) {
    return '';
  }

  const nameParts = name.trim().split(' ');
  const firstLetter = nameParts[0]?.charAt(0).toUpperCase();
  const lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : '';
  return `${firstLetter}. ${lastName}`;
}
