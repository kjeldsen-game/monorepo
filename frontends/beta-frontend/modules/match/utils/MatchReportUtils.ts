import { DuelResultRange } from '../types/MatchResponses';

export const getRangeLabel = (
  value: number | undefined,
  ranges: DuelResultRange[],
) => {
  if (!value) return { label: 'Unknown', color: '#000000' };
  if (value < ranges[0].min) {
    return { label: ranges[0].label, color: ranges[0].color };
  } else if (value > ranges[ranges.length - 1].max) {
    return {
      label: ranges[ranges.length - 1].label,
      color: ranges[ranges.length - 1].color,
    };
  }

  const range = ranges.find((r) => value >= r.min && value <= r.max);
  return range
    ? { label: range.label, color: range.color }
    : { label: 'Unknown', color: '#000000' };
};

export const formatClock = (clock: number): string => {
  const totalMinutes = Math.floor(clock / 4);
  const seconds = (clock % 4) * 15;

  const formattedMinutes = String(totalMinutes).padStart(2, '0');
  const formattedSeconds = String(seconds).padStart(2, '0');

  return `${formattedMinutes}:${formattedSeconds}`;
};
