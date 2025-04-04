export function formatName(name?: string): string {
  if (!name) {
    return '';
  }

  const nameParts = name.trim().split(' ');
  const firstLetter = nameParts[0]?.charAt(0).toUpperCase();
  const lastName = nameParts.length > 1 ? nameParts[nameParts.length - 1] : '';
  return `${firstLetter}. ${lastName}`;
}

export const formatClock = (clock: number): string => {
  const totalMinutes = Math.floor(clock / 4);
  const seconds = (clock % 4) * 15;

  const formattedMinutes = String(totalMinutes).padStart(2, '0');
  const formattedSeconds = String(seconds).padStart(2, '0');

  return `${formattedMinutes}:${formattedSeconds}`;
};
