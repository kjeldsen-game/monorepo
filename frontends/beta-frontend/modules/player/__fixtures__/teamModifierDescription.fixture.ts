import { ModifierDescription } from "@/shared/models/player/TeamModifiers";
import { Tactic } from "../types/TeamModifiers";

export const mockModifierDescriptionFull: ModifierDescription = {
  name: Tactic.CATENACCIO,
  effect: "Increase tempo and intensity",
  pros: "Forces opponent mistakes",
  cons: "Leaves space behind defense",
  purpose: "Disrupt opponent build-up",
};

export const mockModifierDescriptionMinimal: ModifierDescription = {
  name: "MidBlock" as any,
};

export const mockModifierDescriptionWithEffectOnly: ModifierDescription = {
  name: "CompactShape" as any,
  effect: "Team stays narrow",
};
