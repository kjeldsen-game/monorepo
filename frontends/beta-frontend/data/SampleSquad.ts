type Position = 'GOALKEEPER' | 'DEFENDER' | 'MIDDLE' | 'FORWARD'

// Seeded RNG so server and client render the same "random" team (avoids hydration mismatch).
function mulberry32(seed: number) {
  let a = seed
  return () => {
    a |= 0
    a = (a + 0x6d2b79f5) | 0
    let t = Math.imul(a ^ (a >>> 15), 1 | a)
    t = (t + Math.imul(t ^ (t >>> 7), 61 | t)) ^ t
    return ((t ^ (t >>> 14)) >>> 0) / 4294967296
  }
}

const rand = mulberry32(20260616)
const randInt = (min: number, max: number) => Math.floor(min + rand() * (max - min + 1))
const pick = <T,>(arr: T[]) => arr[randInt(0, arr.length - 1)]

const firstNames = [
  'Marco', 'Diego', 'Luca', 'Mateo', 'Bruno', 'Tomas', 'Felix', 'Nico', 'Hugo', 'Theo',
  'Rafael', 'Sergio', 'Mario', 'Pablo', 'Gianni', 'Andres', 'Victor', 'Leon', 'Adan', 'Ivo',
]
const lastNames = [
  'Ferreira', 'Castillo', 'Moretti', 'Navarro', 'Russo', 'Vidal', 'Aguilar', 'Bianchi', 'Soares', 'Mendes',
  'Ortega', 'Pellegrini', 'Salazar', 'Conti', 'Reyes', 'Falcone', 'Duarte', 'Sanabria', 'Marrero', 'Bravo',
]

function randomName(used: Set<string>): string {
  let name = `${pick(firstNames)} ${pick(lastNames)}`
  while (used.has(name)) name = `${pick(firstNames)} ${pick(lastNames)}`
  used.add(name)
  return name
}

const skill = (actual: number) => ({ PlayerSkills: { actual } })

const skillRanges: Record<Position, Record<string, [number, number]>> = {
  GOALKEEPER: { DEFENSE_POSITION: [75, 95], BALL_CONTROL: [5, 25], SCORE: [1, 2], PASSING: [10, 30], OFFENSIVE_POSITION: [1, 10], TACKLING: [5, 20], CO: [60, 90] },
  DEFENDER: { DEFENSE_POSITION: [65, 92], BALL_CONTROL: [15, 45], SCORE: [1, 5], PASSING: [25, 55], OFFENSIVE_POSITION: [5, 25], TACKLING: [60, 92], CO: [25, 50] },
  MIDDLE: { DEFENSE_POSITION: [15, 50], BALL_CONTROL: [60, 92], SCORE: [2, 20], PASSING: [65, 92], OFFENSIVE_POSITION: [45, 80], TACKLING: [25, 60], CO: [15, 35] },
  FORWARD: { DEFENSE_POSITION: [2, 20], BALL_CONTROL: [40, 75], SCORE: [15, 40], PASSING: [15, 40], OFFENSIVE_POSITION: [80, 98], TACKLING: [2, 20], CO: [5, 18] },
}

function buildPlayer(id: number, name: string, position: Position) {
  const ranges = skillRanges[position]
  const actualSkills: Record<string, ReturnType<typeof skill>> = {}
  Object.entries(ranges).forEach(([key, [min, max]]) => {
    actualSkills[key] = skill(randInt(min, max))
  })
  return {
    id,
    name,
    age: randInt(18, 35),
    position,
    actualSkills,
  }
}

function generateSquad() {
  const used = new Set<string>()
  const roster: Position[] = [
    'GOALKEEPER', 'GOALKEEPER',
    'DEFENDER', 'DEFENDER', 'DEFENDER', 'DEFENDER', 'DEFENDER',
    'MIDDLE', 'MIDDLE', 'MIDDLE', 'MIDDLE', 'MIDDLE',
    'FORWARD', 'FORWARD', 'FORWARD',
  ]
  return roster.map((position, index) => buildPlayer(index + 1, randomName(used), position))
}

const teamNames = ['C. D. Al Golpito', 'Real Centellas', 'Atletico Vendaval', 'Union Marejada']
const positions = ['1st', '2nd', '3rd', '4th', '5th', '6th', '7th']

export const SampleSquad = generateSquad()
export const SampleSquadTeamName = pick(teamNames)
export const SampleSquadStanding = pick(positions)
