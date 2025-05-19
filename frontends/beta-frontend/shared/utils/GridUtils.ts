import { Player } from '../models/player/Player';
import { TABLE_PLAYER_POSITION_ORDER } from '../models/player/PlayerPosition';

export function positionComparator(a: Player, b: Player): number {
  // console.log('Comparing:', a, b);

  const indexA = TABLE_PLAYER_POSITION_ORDER.indexOf(a.preferredPosition);
  const indexB = TABLE_PLAYER_POSITION_ORDER.indexOf(b.preferredPosition);

  if (indexA !== indexB) {
    return (
      (indexA === -1 ? Number.MAX_SAFE_INTEGER : indexA) -
      (indexB === -1 ? Number.MAX_SAFE_INTEGER : indexB)
    );
  }

  return a.name.localeCompare(b.name);
}
