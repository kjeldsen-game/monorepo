export interface ITactic {
  name: string
  when: 'in-possession' | 'out-of-possession' | 'transitioning-to-defense' | 'transitioning-to-offense'
  where: Array<'back' | 'mid' | 'front'>
  modifiers: Array<
    | 'wide'
    | 'narrow'
    | 'compact'
    | 'stretched'
    | 'aggressive'
    | 'passive'
    | 'defensive'
    | 'cautious'
    | 'focus-on-ball'
    | 'focus-on-players'
    | 'focus-on-position'
    | 'short-pass'
    | 'long-pass'
    | 'play-from-back'
    | 'play-from-mid'
    | 'play-into-wings'
    | 'play-into-center'
  >
}

export const GengenPress: ITactic = {
  name: 'gengen-press',
  when: 'transitioning-to-defense',
  where: ['front', 'mid'],
  modifiers: ['aggressive', 'focus-on-ball'],
}

export const HighPress: ITactic = {
  name: 'high-press',
  when: 'transitioning-to-defense',
  where: ['front', 'mid'],
  modifiers: ['compact', 'focus-on-players'],
}

export const RecoverDefensiveShape: ITactic = {
  name: 'recover-defensive-shape',
  when: 'transitioning-to-defense',
  where: ['mid', 'back'],
  modifiers: ['passive', 'defensive', 'focus-on-position'],
}

export const RecoverOffensiveShape: ITactic = {
  name: 'recover-offensive-shape',
  when: 'transitioning-to-offense',
  where: ['mid', 'back', 'front'],
  modifiers: ['cautious', 'focus-on-position'],
}

export const MidBlock: ITactic = {
  name: 'mid-block',
  when: 'out-of-possession',
  where: ['mid'],
  modifiers: ['compact', 'focus-on-players'],
}

export const LowBlock: ITactic = {
  name: 'low-block',
  when: 'out-of-possession',
  where: ['back'],
  modifiers: ['passive', 'compact', 'focus-on-players'],
}

export const Zone: ITactic = {
  name: 'zone',
  when: 'out-of-possession',
  where: ['back', 'mid'],
  modifiers: ['passive', 'focus-on-position'],
}

export const PossessionControl: ITactic = {
  name: 'possession-control',
  when: 'in-possession',
  where: ['back', 'mid', 'front'],
  modifiers: ['defensive', 'focus-on-ball', 'wide', 'play-from-back'],
}
