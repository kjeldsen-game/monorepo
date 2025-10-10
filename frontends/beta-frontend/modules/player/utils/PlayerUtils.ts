import { Player } from '@/shared/models/player/Player';

export const filterPlayersByField = <T extends keyof Player>(
  data: Player[] = [],
  field: T,
  value: Player[T],
): Player[] => {
  if (data === null || data == undefined) return;
  return data?.filter((player) => player[field] === value);
};
