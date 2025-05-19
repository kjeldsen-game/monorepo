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

export const formatName = (name: string): string =>
  name.trim().includes(' ')
    ? `${name.trim()[0].toUpperCase()}. ${name.trim().split(' ').slice(1).join(' ')}`
    : name;
