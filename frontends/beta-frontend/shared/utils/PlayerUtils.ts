import { PlayerPosition } from '../models/PlayerPosition';

export function getPositionInitials(position: string | undefined): string {
  if (position == undefined) {
    return '';
  }

  return position
    .split('_')
    .map((word) => word.charAt(0).toUpperCase())
    .join('');
}
