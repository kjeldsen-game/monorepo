export interface TeamModifiers {
  tactic: Tactic;
  verticalPressure: VerticalPressure;
  horizontalPressure: HorizontalPressure;
}

export enum Tactic {
  DOUBLE_TEAM = 'DOUBLE_TEAM',
  MAN_ON_MAN = 'MAN_ON_MAN',
  ZONE = 'ZONE',
  COUNTER_ATTACK = 'COUNTER_ATTACK',
  POSSESSION_CONTROL = 'POSSESSION_CONTROL',
  TIKA_TAKA = 'TIKA_TAKA',
  WING_PLAY = 'WING_PLAY',
  CATENACCIO = 'CATENACCIO',
  ROUTE_ONE = 'ROUTE_ONE',
  OFFSIDE_TRAP = 'OFFSIDE_TRAP',
}

export enum VerticalPressure {
  HIGH_PRESSURE = 'HIGH_PRESSURE',
  MID_PRESSURE = 'MID_PRESSURE',
  LOW_PRESSURE = 'LOW_PRESSURE',
  NO_VERTICAL_FOCUS = 'NO_VERTICAL_FOCUS',
}

export enum HorizontalPressure {
  SWARM_CENTRE = 'SWARM_CENTRE',
  SWARM_FLANKS = 'SWARM_FLANKS',
  NO_HORIZONTAL_FOCUS = 'NO_HORIZONTAL_FOCUS',
}

export interface ModifierDescription {
  name: Tactic | VerticalPressure | HorizontalPressure;
  effect?: string;
  pros?: string;
  cons?: string;
  purpose?: string;
}

export const TacticDescriptions: ModifierDescription[] = [
  {
    name: Tactic.POSSESSION_CONTROL,
    effect:
      'Neutral midfielders contribute 5% additional influence to both offensive and defensive zones. Offensive midfielders contribute 3% more.',
    purpose:
      'Ideal for teams that prioritize control in midfield and balanced play across zones.',
  },
  {
    name: Tactic.TIKA_TAKA,
    effect:
      'Doubles the likelihood of central midfielders receiving the ball compared to flank players.',
    purpose:
      'Focuses on short passes and central control to maintain possession and build play methodically.',
  },
  {
    name: Tactic.WING_PLAY,
    effect:
      'Doubles the likelihood of flank players receiving the ball compared to central players.',
    purpose:
      'Targets wide areas to exploit space and deliver crosses into the box.',
  },
  {
    name: Tactic.ZONE,
    pros: 'Provides a 15% increase in defensive and offensive assistance within the penalty box.',
    cons: 'Increases the chance of selecting the wrong central defender as the active main defender by 50%.',
    purpose:
      'Strengthens the penalty area defense at the risk of occasional miscommunication.',
  },
  {
    name: Tactic.DOUBLE_TEAM,
    pros: "The closest defender joins the main active defender, increasing the main defender's assistance by 25%. If the attacker beats the first defender, they immediately face a second defender with an additional 25% defensive assistance.",
    cons: 'If the attacker succeeds against both defenders, they become completely unmarked against the goalkeeper. Other attackers in the area lose 50% of defensive assistance.',
    purpose:
      'Provides aggressive defensive support but risks high exposure if unsuccessful.',
  },
  {
    name: Tactic.MAN_ON_MAN,
    pros: 'Reduces assistance within the penalty box by 15%, enhancing individual defensive coverage.',
    cons: 'Decreases the likelihood of selecting the wrong central defender as the active main defender by 50%.',
    purpose:
      'Tightens marking of individual attackers, ideal for countering key opposition threats.',
  },
  {
    name: Tactic.COUNTER_ATTACK,
    effect:
      'Defensive contribution increases by 20%, while offensive contribution decreases by 10%.',
    purpose:
      'Focuses on soaking up pressure and quickly transitioning to attack, ideal for teams prioritizing defensive stability and fast breaks.',
  },
];

export const VerticalPressureDescriptions: ModifierDescription[] = [
  {
    name: VerticalPressure.HIGH_PRESSURE,
    pros: 'Defensive assistance in the center is increased by 10%, and the chances of a counterattack when stealing the ball in midfield are doubled.',
    cons: 'Defensive assistance in the penalty box is decreased by 10%, making it easier for the opposition to get in dangerous areas.',
  },
  {
    name: VerticalPressure.MID_PRESSURE,
    pros: 'Defensive assistance in the center is increased by 10%, and the chances of a counterattack when stealing the ball in midfield are doubled.',
    cons: 'Defensive assistance in the penalty box is decreased by 10%, making it easier for the opposition to get in dangerous areas.',
  },
  {
    name: VerticalPressure.LOW_PRESSURE,
    pros: 'Defensive assistance in the penalty box is increased by 10%, and the chances of a counterattack when stealing the ball in midfield are doubled.',
    cons: 'Defensive assistance in the middle is decreased by 10%, making it harder to protect the central areas.',
  },
  {
    name: VerticalPressure.NO_VERTICAL_FOCUS,
    pros: 'Balanced defensive approach with no emphasis on a specific zone.',
    cons: 'No specific advantage or disadvantage, making it less impactful in certain scenarios.',
  },
];

export const HorizontalPressureDescriptions: ModifierDescription[] = [
  {
    name: HorizontalPressure.SWARM_CENTRE,
    pros: '+15% assistance in center defense (penalty box and midfield). Modifies chances of the selected attacker module to enhance central defense.',
    cons: '-30% assistance in flanks (penalty box and midfield), leaving the wide areas more vulnerable to attacks.',
  },
  {
    name: HorizontalPressure.SWARM_FLANKS,
    pros: '+15% assistance in flank defense (penalty box and midfield). Shifts 25% of player focus to the center, strengthening central defense.',
    cons: '-30% assistance in midfield (penalty box and midfield), weakening coverage in the middle of the field.',
  },
  {
    name: HorizontalPressure.NO_HORIZONTAL_FOCUS,
    pros: 'Balanced defensive approach with no emphasis on a specific zone.',
    cons: 'No specific advantage or disadvantage, maintaining a neutral defensive setup.',
  },
];
