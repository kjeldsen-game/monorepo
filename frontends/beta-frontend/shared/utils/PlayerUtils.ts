import { PlayerPositionAbbreviation } from '../models/PlayerPosition';

export function getPositionInitials(position: string | undefined): string {
  if (!position) return '';

  return (
    PlayerPositionAbbreviation[
      position as keyof typeof PlayerPositionAbbreviation
    ] || ''
  );
}
