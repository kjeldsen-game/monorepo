import { PlayerPositionAbbreviation } from '../models/player/PlayerPosition';

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

export const formatName = (name: string): string => {
  if (!name) return '';

  const parts = name.trim().split(' ');

  const firstInitial = parts[0][0].toUpperCase();
  const lastTwoSurnames = parts.slice(-2).join(' ');

  return `${firstInitial}. ${getSurname(name)}`;
};
