import { PlayerPositionAbbreviation } from '../models/PlayerPosition';

export function getPositionInitials(position: string | undefined): string {
  if (!position) return '';

  return (
    PlayerPositionAbbreviation[
      position as keyof typeof PlayerPositionAbbreviation
    ] || ''
  );
}

export const getSurname = (name: string): string => {
  if (!name) return '';

  const nameParts = name.split(' ');
  return nameParts.length > 1 ? nameParts[nameParts.length - 1] : '';
};
