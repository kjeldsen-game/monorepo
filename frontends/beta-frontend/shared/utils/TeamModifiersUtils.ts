import {
  HorizontalPressure,
  HorizontalPressureDescriptions,
  ModifierDescription,
  Tactic,
  TacticDescriptions,
  VerticalPressure,
  VerticalPressureDescriptions,
} from '../models/TeamModifiers';

export function getModifierDescription(
  name: HorizontalPressure | VerticalPressure | Tactic | undefined,
): ModifierDescription | undefined {
  if (!name) return undefined;

  const pressureDescription = [
    ...TacticDescriptions,
    ...HorizontalPressureDescriptions,
    ...VerticalPressureDescriptions,
  ].find((pressure) => pressure.name === name);

  if (!pressureDescription) {
    console.warn(`No pressure description found for ${name}`);
    return undefined;
  }

  return pressureDescription;
}
