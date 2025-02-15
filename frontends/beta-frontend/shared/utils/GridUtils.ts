import { Player } from '../models/Player';
import { TABLE_PLAYER_POSITION_ORDER } from '../models/PlayerPosition';

export function positionComparator(a: Player, b: Player): number {
  // console.log('Comparing:', a, b);

  const indexA = TABLE_PLAYER_POSITION_ORDER.indexOf(a.position);
  const indexB = TABLE_PLAYER_POSITION_ORDER.indexOf(b.position);

  if (indexA !== indexB) {
    return (
      (indexA === -1 ? Number.MAX_SAFE_INTEGER : indexA) -
      (indexB === -1 ? Number.MAX_SAFE_INTEGER : indexB)
    );
  }

  return a.name.localeCompare(b.name);
}
