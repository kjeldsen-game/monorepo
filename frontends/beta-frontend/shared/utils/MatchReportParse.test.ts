import { MatchReportType } from '../models/MatchReport'
import { PlayerPosition } from '../models/PlayerPosition'
import { parseReport } from './MatchReportParser'

describe('Match report parser tests', () => {
  it('Check player lineup', () => {
    expect.assertions(2)
    const result = parseReport(matchMock)
    expect(result.homePlayers).toEqual(expectedHomePlayers)
    expect(result.awayPlayers).toEqual(expectedAwayPlayers)
  })

  it('Check player events', () => {
    expect.assertions(2)
    const result = parseReport(matchMock)
    expect(result.events[0]).toEqual(expectedEventPos0)
    expect(result.events[88]).toEqual(expectedEventPos88)
  })
})

const expectedHomePlayers = [
  { name: 'Madisen Schamberger', position: PlayerPosition['LEFT_BACK'] },
  { name: 'Maybelle Beahan', position: PlayerPosition['RIGHT_BACK'] },
  { name: 'Mr. Vicente Murphy', position: PlayerPosition['CENTRE_BACK'] },
  { name: 'Miss Sandy Ernser', position: PlayerPosition['LEFT_MIDFIELDER'] },
  { name: 'Brendon Hermann', position: PlayerPosition['RIGHT_MIDFIELDER'] },
  { name: 'Mrs. Elwyn Wehner', position: PlayerPosition['CENTRE_MIDFIELDER'] },
  { name: 'Jillian Pouros', position: PlayerPosition['LEFT_WINGER'] },
  { name: 'Sydney Lowe', position: PlayerPosition['RIGHT_WINGER'] },
  { name: 'Amanda Spinka', position: PlayerPosition['STRIKER'] },
  { name: 'Carlie Corwin', position: PlayerPosition['STRIKER'] },
  { name: 'Lemuel Grant', position: PlayerPosition['GOALKEEPER'] },
]
const expectedAwayPlayers = [
  { name: 'Miss Cassie Padberg', position: PlayerPosition['LEFT_BACK'] },
  { name: 'Annabelle Douglas', position: PlayerPosition['RIGHT_BACK'] },
  { name: 'Kari Durgan', position: PlayerPosition['CENTRE_BACK'] },
  { name: 'Ada Boehm', position: PlayerPosition['LEFT_MIDFIELDER'] },
  { name: 'Patricia Sporer MD', position: PlayerPosition['RIGHT_MIDFIELDER'] },
  { name: 'Dr. Coty Johnston', position: PlayerPosition['CENTRE_MIDFIELDER'] },
  { name: 'Justice Armstrong', position: PlayerPosition['LEFT_WINGER'] },
  { name: 'Wyatt Hartmann', position: PlayerPosition['RIGHT_WINGER'] },
  { name: 'Chyna Yundt', position: PlayerPosition['STRIKER'] },
  { name: 'Ms. Holden Walsh', position: PlayerPosition['STRIKER'] },
  { name: 'Ned Padberg', position: PlayerPosition['GOALKEEPER'] },
]

const expectedEventPos0 = {
  clock: 1,
  area: 'CENTRE_MIDFIELD',
  action: 'PASS',
  eventStart: 'Amanda Spinka [STRIKER] made an awesome pass to Brendon Hermann [RIGHT_MIDFIELDER]',
  eventResponse: 'Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made a poor attempt to intercept the ball',
  eventResult: 'The pass was successful',
  eventSide: 'HomeTeamEvent',
  actionStats: {
    player1: { skillContribution: 45, performance: 100, total: 145 },
    player2: { skillContribution: 46, performance: 25, total: 71 },
  },
}

const expectedEventPos88 = {
  clock: 89,
  area: 'LEFT_MIDFIELD',
  action: 'POSITION',
  eventStart: 'Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free',
  eventResponse: 'Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close',
  eventResult: 'Justice Armstrong won, he was far',
  eventSide: 'AwayTeamEvent',
  actionStats: {
    player1: { skillContribution: 82, performance: 9, total: 102, assistance: 11 },
    player2: { skillContribution: 73, performance: -13, total: 71, assistance: 11 },
  },
}

const matchMock = `# Home team players

Madisen Schamberger [LEFT_BACK]
Maybelle Beahan [RIGHT_BACK]
Mr. Vicente Murphy [CENTRE_BACK]
Miss Sandy Ernser [LEFT_MIDFIELDER]
Brendon Hermann [RIGHT_MIDFIELDER]
Mrs. Elwyn Wehner [CENTRE_MIDFIELDER]
Jillian Pouros [LEFT_WINGER]
Sydney Lowe [RIGHT_WINGER]
Amanda Spinka [STRIKER]
Carlie Corwin [STRIKER]
Lemuel Grant [GOALKEEPER]

# Away team players

Miss Cassie Padberg [LEFT_BACK]
Annabelle Douglas [RIGHT_BACK]
Kari Durgan [CENTRE_BACK]
Ada Boehm [LEFT_MIDFIELDER]
Patricia Sporer MD [RIGHT_MIDFIELDER]
Dr. Coty Johnston [CENTRE_MIDFIELDER]
Justice Armstrong [LEFT_WINGER]
Wyatt Hartmann [RIGHT_WINGER]
Chyna Yundt [STRIKER]
Ms. Holden Walsh [STRIKER]
Ned Padberg [GOALKEEPER]

> Kicking off game...
> Starting player is Amanda Spinka
> First half starting...
> Half time. Result: 1 - 4
> Second half starting...
> Match ended. Result: 2 - 7

# Match report

Clock: 1
Area: CENTRE_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made an awesome pass to Brendon Hermann [RIGHT_MIDFIELDER]
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made a poor attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 100
Total: 145
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: 25
Total: 71

Clock: 2
Area: RIGHT_MIDFIELD
Action: POSITION
Brendon Hermann [RIGHT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful attempt to stay close
Brendon Hermann lost, he was near
POSITIONAL Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 45
Performance: -6
Assistance: 14
Total: 53
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: 1
Assistance: 11
Total: 58

Clock: 3
Area: CENTRE_MIDFIELD
Action: TACKLE
Dr. Coty Johnston [CENTRE_MIDFIELDER] attempted a brilliant tackle
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made a poor dribble
Dr. Coty Johnston controlled the ball
BALL_CONTROL Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 77
Performance: 84
Carry over: 5
Total: 166
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 22
Total: 95

Clock: 4
Area: CENTRE_MIDFIELD
Action: SHOOT
Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awesome shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted an awesome save
Lemuel Grant saved the ball
SHOT Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 42
Performance: 100
Total: 142
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 97
Total: 171

Clock: 5
Area: CENTRE_BACK
Action: PASS
Lemuel Grant [GOALKEEPER] made a poor pass to Miss Sandy Ernser [LEFT_MIDFIELDER]
Duel challenger: Kari Durgan [CENTRE_BACK] made a masterful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Lemuel Grant ──────────────────
Skill contribution: 79
Performance: 16
Total: 95
───────────────── Kari Durgan ──────────────────
Skill contribution: 91
Performance: 102
Total: 193

Clock: 6
Area: LEFT_MIDFIELD
Action: POSITION
Miss Sandy Ernser [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful attempt to stay close
Miss Sandy Ernser won, he was there
POSITIONAL Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: -1
Assistance: 12
Total: 101
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: -3
Assistance: 10
Total: 53

Clock: 7
Area: LEFT_MIDFIELD
Action: SHOOT
Miss Sandy Ernser [LEFT_MIDFIELDER] made a superb shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted an unbelievable save
Ned Padberg saved the ball
SHOT Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 78
Total: 168
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 113
Total: 202

Clock: 8
Area: CENTRE_BACK
Action: PASS
Ned Padberg [GOALKEEPER] made an awful pass to Ada Boehm [LEFT_MIDFIELDER]
Duel challenger: Mr. Vicente Murphy [CENTRE_BACK] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ned Padberg ──────────────────
Skill contribution: 88
Performance: 7
Total: 95
───────────────── Mr. Vicente Murphy ──────────────────
Skill contribution: 46
Performance: 6
Total: 52

Clock: 9
Area: LEFT_MIDFIELD
Action: POSITION
Ada Boehm [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Ada Boehm won, he was close
POSITIONAL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 10
Assistance: 12
Total: 95
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: -9
Assistance: 14
Total: 53

Clock: 10
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a good pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 56
Total: 149
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 5
Total: 53

Clock: 11
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was close
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: 7
Assistance: 14
Total: 103
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: -6
Assistance: 15
Total: 57

Clock: 12
Area: LEFT_MIDFIELD
Action: SHOOT
Justice Armstrong [LEFT_WINGER] made an awful shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted an awful save
Justice Armstrong scored a goal
SHOT Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 59
Performance: 3
Total: 62
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 7
Total: 81

Clock: 13
Area: LEFT_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made a superb pass to Sydney Lowe [RIGHT_WINGER]
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made a superb attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 72
Total: 117
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 79
Total: 164

Clock: 14
Area: RIGHT_MIDFIELD
Action: POSITION
Sydney Lowe [RIGHT_WINGER] tried with an awful effort to get free
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made an awful attempt to stay close
Sydney Lowe lost, he was ready
POSITIONAL Duel stats:
───────────────── Sydney Lowe ──────────────────
Skill contribution: 57
Performance: -15
Assistance: 13
Total: 55
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 9
Assistance: 8
Total: 79

Clock: 15
Area: RIGHT_MIDFIELD
Action: TACKLE
Patricia Sporer MD [RIGHT_MIDFIELDER] attempted an awful tackle
Duel challenger: Sydney Lowe [RIGHT_WINGER] made a decent dribble
Sydney Lowe stole the ball
BALL_CONTROL Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 79
Performance: 6
Carry over: 24
Total: 109
───────────────── Sydney Lowe ──────────────────
Skill contribution: 81
Performance: 48
Total: 129

Clock: 16
Area: RIGHT_MIDFIELD
Action: SHOOT
Sydney Lowe [RIGHT_WINGER] made a superb shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted an excellent save
Sydney Lowe scored a goal
SHOT Duel stats:
───────────────── Sydney Lowe ──────────────────
Skill contribution: 40
Performance: 80
Total: 120
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 69
Total: 158

Clock: 17
Area: RIGHT_MIDFIELD
Action: PASS
Chyna Yundt [STRIKER] made a decent pass to Ada Boehm [LEFT_MIDFIELDER]
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made a good attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Chyna Yundt ──────────────────
Skill contribution: 77
Performance: 45
Total: 122
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 60
Total: 133

Clock: 18
Area: LEFT_MIDFIELD
Action: POSITION
Ada Boehm [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Ada Boehm won, he was ready
POSITIONAL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 7
Assistance: 13
Total: 93
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 8
Assistance: 10
Total: 66

Clock: 19
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made an excellent pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an unbelievable attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 63
Total: 156
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 116
Total: 164

Clock: 20
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was far
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: 2
Assistance: 11
Total: 95
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: -10
Assistance: 13
Total: 76

Clock: 21
Area: LEFT_MIDFIELD
Action: SHOOT
Justice Armstrong [LEFT_WINGER] made an excellent shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted a masterful save
Justice Armstrong scored a goal
SHOT Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 59
Performance: 62
Total: 121
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 102
Total: 176

Clock: 22
Area: LEFT_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made a decent pass to Miss Sandy Ernser [LEFT_MIDFIELDER]
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made an awesome attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 49
Total: 94
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 95
Total: 180

Clock: 23
Area: LEFT_MIDFIELD
Action: POSITION
Miss Sandy Ernser [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made an awful attempt to stay close
Miss Sandy Ernser lost, he was in perfect position
POSITIONAL Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 5
Assistance: 14
Total: 109
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 13
Assistance: 13
Total: 111

Clock: 24
Area: LEFT_MIDFIELD
Action: TACKLE
Ada Boehm [LEFT_MIDFIELDER] attempted a poor tackle
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made a decent dribble
Miss Sandy Ernser stole the ball
BALL_CONTROL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 17
Carry over: 2
Total: 92
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 48
Total: 96

Clock: 25
Area: LEFT_MIDFIELD
Action: PASS
Miss Sandy Ernser [LEFT_MIDFIELDER] made an awesome pass to Brendon Hermann [RIGHT_MIDFIELDER]
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made a weak attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 99
Total: 189
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 35
Total: 120

Clock: 26
Area: RIGHT_MIDFIELD
Action: POSITION
Brendon Hermann [RIGHT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made an awful attempt to stay close
Brendon Hermann lost, he was near
POSITIONAL Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 45
Performance: 2
Assistance: 14
Total: 61
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 3
Assistance: 11
Total: 76

Clock: 27
Area: RIGHT_MIDFIELD
Action: TACKLE
Patricia Sporer MD [RIGHT_MIDFIELDER] attempted a weak tackle
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made a masterful dribble
Brendon Hermann stole the ball
BALL_CONTROL Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 79
Performance: 40
Carry over: 15
Total: 134
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 109
Total: 182

Clock: 28
Area: RIGHT_MIDFIELD
Action: SHOOT
Brendon Hermann [RIGHT_MIDFIELDER] made a superb shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted an awful save
Ned Padberg saved the ball
SHOT Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 81
Performance: 79
Total: 160
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 11
Total: 100

Clock: 29
Area: CENTRE_BACK
Action: PASS
Ned Padberg [GOALKEEPER] made an excellent pass to Ada Boehm [LEFT_MIDFIELDER]
Duel challenger: Mr. Vicente Murphy [CENTRE_BACK] made a poor attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ned Padberg ──────────────────
Skill contribution: 88
Performance: 69
Total: 157
───────────────── Mr. Vicente Murphy ──────────────────
Skill contribution: 46
Performance: 24
Total: 70

Clock: 30
Area: LEFT_MIDFIELD
Action: POSITION
Ada Boehm [LEFT_MIDFIELDER] tried with a poor effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Ada Boehm won, he was close
POSITIONAL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 18
Assistance: 15
Total: 106
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: -7
Assistance: 13
Total: 79

Clock: 31
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a decent pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an unbelievable attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 41
Total: 134
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 119
Total: 167

Clock: 32
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was there
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: -11
Assistance: 14
Total: 85
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: -1
Assistance: 12
Total: 84

Clock: 33
Area: LEFT_MIDFIELD
Action: PASS
Justice Armstrong [LEFT_WINGER] made a weak pass to Wyatt Hartmann [RIGHT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made a masterful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 59
Performance: 37
Total: 96
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 108
Total: 156

Clock: 34
Area: RIGHT_MIDFIELD
Action: POSITION
Wyatt Hartmann [RIGHT_WINGER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Wyatt Hartmann won, he was close
POSITIONAL Duel stats:
───────────────── Wyatt Hartmann ──────────────────
Skill contribution: 50
Performance: -4
Assistance: 12
Total: 58
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: -8
Assistance: 15
Total: 55

Clock: 35
Area: RIGHT_MIDFIELD
Action: SHOOT
Wyatt Hartmann [RIGHT_WINGER] made an awful shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted a weak save
Wyatt Hartmann scored a goal
SHOT Duel stats:
───────────────── Wyatt Hartmann ──────────────────
Skill contribution: 92
Performance: 9
Total: 101
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 34
Total: 108

Clock: 36
Area: RIGHT_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made a masterful pass to Miss Sandy Ernser [LEFT_MIDFIELDER]
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 104
Total: 149
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 1
Total: 63

Clock: 37
Area: LEFT_MIDFIELD
Action: POSITION
Miss Sandy Ernser [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful attempt to stay close
Miss Sandy Ernser won, he was very far
POSITIONAL Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: -6
Assistance: 11
Total: 95
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: -15
Assistance: 11
Total: 42

Clock: 38
Area: LEFT_MIDFIELD
Action: SHOOT
Miss Sandy Ernser [LEFT_MIDFIELDER] made a poor shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted a weak save
Ned Padberg saved the ball
SHOT Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 27
Total: 117
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 38
Total: 127

Clock: 39
Area: CENTRE_BACK
Action: PASS
Ned Padberg [GOALKEEPER] made a good pass to Ada Boehm [LEFT_MIDFIELDER]
Duel challenger: Mr. Vicente Murphy [CENTRE_BACK] made a brilliant attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ned Padberg ──────────────────
Skill contribution: 88
Performance: 58
Total: 146
───────────────── Mr. Vicente Murphy ──────────────────
Skill contribution: 46
Performance: 82
Total: 128

Clock: 40
Area: LEFT_MIDFIELD
Action: POSITION
Ada Boehm [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Ada Boehm won, he was far
POSITIONAL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: -11
Assistance: 14
Total: 76
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: -13
Assistance: 10
Total: 70

Clock: 41
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a poor pass to Dr. Coty Johnston [CENTRE_MIDFIELDER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made a brilliant attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 22
Total: 115
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 81
Total: 129

Clock: 42
Area: CENTRE_MIDFIELD
Action: POSITION
Dr. Coty Johnston [CENTRE_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Dr. Coty Johnston won, he was far
POSITIONAL Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 77
Performance: 0
Assistance: 11
Total: 88
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: -13
Assistance: 10
Total: 70

Clock: 43
Area: CENTRE_MIDFIELD
Action: SHOOT
Dr. Coty Johnston [CENTRE_MIDFIELDER] made a masterful shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted a brilliant save
Dr. Coty Johnston scored a goal
SHOT Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 42
Performance: 110
Total: 152
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 89
Total: 163

Clock: 44
Area: CENTRE_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made an awesome pass to Brendon Hermann [RIGHT_MIDFIELDER]
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made a masterful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 91
Total: 136
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: 108
Total: 154

Clock: 45
Area: RIGHT_MIDFIELD
Action: POSITION
Brendon Hermann [RIGHT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made an awful attempt to stay close
Brendon Hermann lost, he was ready
POSITIONAL Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 45
Performance: 6
Assistance: 9
Total: 60
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 9
Assistance: 17
Total: 88

Clock: 46
Area: RIGHT_MIDFIELD
Action: TACKLE
Patricia Sporer MD [RIGHT_MIDFIELDER] attempted a good tackle
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awesome dribble
Brendon Hermann stole the ball
BALL_CONTROL Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 79
Performance: 60
Carry over: 28
Total: 167
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 100
Total: 173

Clock: 47
Area: RIGHT_MIDFIELD
Action: PASS
Brendon Hermann [RIGHT_MIDFIELDER] made a brilliant pass to Sydney Lowe [RIGHT_WINGER]
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 81
Performance: 86
Total: 167
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 11
Total: 73

Clock: 48
Area: RIGHT_MIDFIELD
Action: POSITION
Sydney Lowe [RIGHT_WINGER] tried with an awful effort to get free
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made a poor attempt to stay close
Sydney Lowe lost, he was in perfect position
POSITIONAL Duel stats:
───────────────── Sydney Lowe ──────────────────
Skill contribution: 57
Performance: -5
Assistance: 11
Total: 63
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 22
Assistance: 14
Total: 98

Clock: 49
Area: RIGHT_MIDFIELD
Action: TACKLE
Patricia Sporer MD [RIGHT_MIDFIELDER] attempted an awesome tackle
Duel challenger: Sydney Lowe [RIGHT_WINGER] made an unbelievable dribble
Patricia Sporer MD controlled the ball
BALL_CONTROL Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 79
Performance: 96
Carry over: 35
Total: 210
───────────────── Sydney Lowe ──────────────────
Skill contribution: 81
Performance: 115
Total: 196

Clock: 50
Area: RIGHT_MIDFIELD
Action: SHOOT
Patricia Sporer MD [RIGHT_MIDFIELDER] made a superb shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted an awful save
Patricia Sporer MD scored a goal
SHOT Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 55
Performance: 75
Total: 130
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 12
Total: 86

Clock: 51
Area: RIGHT_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made an excellent pass to Brendon Hermann [RIGHT_MIDFIELDER]
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made a poor attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 61
Total: 106
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 22
Total: 84

Clock: 52
Area: RIGHT_MIDFIELD
Action: POSITION
Brendon Hermann [RIGHT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made an awful attempt to stay close
Brendon Hermann lost, he was in perfect position
POSITIONAL Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 45
Performance: 0
Assistance: 18
Total: 63
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 11
Assistance: 11
Total: 107

Clock: 53
Area: LEFT_MIDFIELD
Action: TACKLE
Ada Boehm [LEFT_MIDFIELDER] attempted a masterful tackle
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made a good dribble
Ada Boehm controlled the ball
BALL_CONTROL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 106
Carry over: 44
Total: 223
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 58
Total: 131

Clock: 54
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a poor pass to Dr. Coty Johnston [CENTRE_MIDFIELDER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made a poor attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 18
Total: 111
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 22
Total: 70

Clock: 55
Area: CENTRE_MIDFIELD
Action: POSITION
Dr. Coty Johnston [CENTRE_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Dr. Coty Johnston won, he was ready
POSITIONAL Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 77
Performance: -9
Assistance: 12
Total: 80
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 7
Assistance: 15
Total: 70

Clock: 56
Area: CENTRE_MIDFIELD
Action: SHOOT
Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted an awesome save
Lemuel Grant saved the ball
SHOT Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 42
Performance: 2
Total: 44
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 97
Total: 171

Clock: 57
Area: CENTRE_BACK
Action: PASS
Lemuel Grant [GOALKEEPER] made an awful pass to Miss Sandy Ernser [LEFT_MIDFIELDER]
Duel challenger: Kari Durgan [CENTRE_BACK] made a decent attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Lemuel Grant ──────────────────
Skill contribution: 79
Performance: 7
Total: 86
───────────────── Kari Durgan ──────────────────
Skill contribution: 91
Performance: 43
Total: 134

Clock: 58
Area: LEFT_MIDFIELD
Action: POSITION
Miss Sandy Ernser [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made a poor attempt to stay close
Miss Sandy Ernser won, he was in perfect position
POSITIONAL Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 7
Assistance: 13
Total: 110
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 19
Assistance: 13
Total: 94

Clock: 59
Area: LEFT_MIDFIELD
Action: SHOOT
Miss Sandy Ernser [LEFT_MIDFIELDER] made an unbelievable shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted a decent save
Ned Padberg saved the ball
SHOT Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 118
Total: 208
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 50
Total: 139

Clock: 60
Area: CENTRE_BACK
Action: PASS
Ned Padberg [GOALKEEPER] made a weak pass to Ada Boehm [LEFT_MIDFIELDER]
Duel challenger: Mr. Vicente Murphy [CENTRE_BACK] made a brilliant attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ned Padberg ──────────────────
Skill contribution: 88
Performance: 34
Total: 122
───────────────── Mr. Vicente Murphy ──────────────────
Skill contribution: 46
Performance: 85
Total: 131

Clock: 61
Area: LEFT_MIDFIELD
Action: POSITION
Ada Boehm [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Mrs. Elwyn Wehner [CENTRE_MIDFIELDER] made an awful attempt to stay close
Ada Boehm won, he was very far
POSITIONAL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 6
Assistance: 14
Total: 93
───────────────── Mrs. Elwyn Wehner ──────────────────
Skill contribution: 76
Performance: -15
Assistance: 15
Total: 76

Clock: 62
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a brilliant pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 81
Total: 174
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 7
Total: 55

Clock: 63
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was in perfect position
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: 15
Assistance: 15
Total: 112
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 13
Assistance: 14
Total: 75

Clock: 64
Area: LEFT_MIDFIELD
Action: SHOOT
Justice Armstrong [LEFT_WINGER] made a decent shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted a weak save
Justice Armstrong scored a goal
SHOT Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 59
Performance: 49
Total: 108
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 40
Total: 114

Clock: 65
Area: LEFT_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made an awful pass to Jillian Pouros [LEFT_WINGER]
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made an unbelievable attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 0
Total: 45
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 116
Total: 201

Clock: 66
Area: LEFT_MIDFIELD
Action: POSITION
Jillian Pouros [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made an awful attempt to stay close
Jillian Pouros lost, he was ready
POSITIONAL Duel stats:
───────────────── Jillian Pouros ──────────────────
Skill contribution: 95
Performance: -6
Assistance: 14
Total: 103
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 8
Assistance: 11
Total: 104

Clock: 67
Area: LEFT_MIDFIELD
Action: TACKLE
Ada Boehm [LEFT_MIDFIELDER] attempted a weak tackle
Duel challenger: Jillian Pouros [LEFT_WINGER] made an awful dribble
Ada Boehm controlled the ball
BALL_CONTROL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 40
Carry over: 1
Total: 114
───────────────── Jillian Pouros ──────────────────
Skill contribution: 70
Performance: 0
Total: 70

Clock: 68
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a poor pass to Patricia Sporer MD [RIGHT_MIDFIELDER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made a weak attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 27
Total: 120
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 37
Total: 85

Clock: 69
Area: RIGHT_MIDFIELD
Action: POSITION
Patricia Sporer MD [RIGHT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Patricia Sporer MD lost, he was ready
POSITIONAL Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 79
Performance: 2
Assistance: 9
Total: 90
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 10
Assistance: 15
Total: 98

Clock: 70
Area: RIGHT_MIDFIELD
Action: TACKLE
Brendon Hermann [RIGHT_MIDFIELDER] attempted an excellent tackle
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made a superb dribble
Patricia Sporer MD stole the ball
BALL_CONTROL Duel stats:
───────────────── Brendon Hermann ──────────────────
Skill contribution: 45
Performance: 63
Carry over: 8
Total: 116
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 71
Total: 133

Clock: 71
Area: RIGHT_MIDFIELD
Action: PASS
Patricia Sporer MD [RIGHT_MIDFIELDER] made an awesome pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made a brilliant attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 55
Performance: 93
Total: 148
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 87
Total: 160

Clock: 72
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was near
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: 7
Assistance: 15
Total: 104
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: 1
Assistance: 11
Total: 85

Clock: 73
Area: LEFT_MIDFIELD
Action: SHOOT
Justice Armstrong [LEFT_WINGER] made a good shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted a poor save
Justice Armstrong scored a goal
SHOT Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 59
Performance: 59
Total: 118
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 28
Total: 102

Clock: 74
Area: LEFT_MIDFIELD
Action: PASS
Amanda Spinka [STRIKER] made an awful pass to Mrs. Elwyn Wehner [CENTRE_MIDFIELDER]
Duel challenger: Ada Boehm [LEFT_MIDFIELDER] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Amanda Spinka ──────────────────
Skill contribution: 45
Performance: 0
Total: 45
───────────────── Ada Boehm ──────────────────
Skill contribution: 85
Performance: 13
Total: 98

Clock: 75
Area: CENTRE_MIDFIELD
Action: POSITION
Mrs. Elwyn Wehner [CENTRE_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful attempt to stay close
Mrs. Elwyn Wehner lost, he was in perfect position
POSITIONAL Duel stats:
───────────────── Mrs. Elwyn Wehner ──────────────────
Skill contribution: 63
Performance: -10
Assistance: 12
Total: 65
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: 13
Assistance: 13
Total: 72

Clock: 76
Area: CENTRE_MIDFIELD
Action: TACKLE
Dr. Coty Johnston [CENTRE_MIDFIELDER] attempted a weak tackle
Duel challenger: Mrs. Elwyn Wehner [CENTRE_MIDFIELDER] made an awesome dribble
Mrs. Elwyn Wehner stole the ball
BALL_CONTROL Duel stats:
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 77
Performance: 34
Carry over: 7
Total: 118
───────────────── Mrs. Elwyn Wehner ──────────────────
Skill contribution: 76
Performance: 98
Total: 174

Clock: 77
Area: CENTRE_MIDFIELD
Action: PASS
Mrs. Elwyn Wehner [CENTRE_MIDFIELDER] made a masterful pass to Miss Sandy Ernser [LEFT_MIDFIELDER]
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Mrs. Elwyn Wehner ──────────────────
Skill contribution: 54
Performance: 104
Total: 158
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: 5
Total: 51

Clock: 78
Area: LEFT_MIDFIELD
Action: POSITION
Miss Sandy Ernser [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Dr. Coty Johnston [CENTRE_MIDFIELDER] made an awful attempt to stay close
Miss Sandy Ernser won, he was there
POSITIONAL Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: -1
Assistance: 13
Total: 102
───────────────── Dr. Coty Johnston ──────────────────
Skill contribution: 46
Performance: -3
Assistance: 15
Total: 58

Clock: 79
Area: LEFT_MIDFIELD
Action: SHOOT
Miss Sandy Ernser [LEFT_MIDFIELDER] made a weak shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted an awful save
Ned Padberg saved the ball
SHOT Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 38
Total: 128
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 0
Total: 89

Clock: 80
Area: CENTRE_BACK
Action: PASS
Ned Padberg [GOALKEEPER] made a decent pass to Ada Boehm [LEFT_MIDFIELDER]
Duel challenger: Mr. Vicente Murphy [CENTRE_BACK] made a poor attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ned Padberg ──────────────────
Skill contribution: 88
Performance: 44
Total: 132
───────────────── Mr. Vicente Murphy ──────────────────
Skill contribution: 46
Performance: 25
Total: 71

Clock: 81
Area: LEFT_MIDFIELD
Action: POSITION
Ada Boehm [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an awful attempt to stay close
Ada Boehm won, he was there
POSITIONAL Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 73
Performance: 0
Assistance: 17
Total: 90
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: -2
Assistance: 13
Total: 59

Clock: 82
Area: LEFT_MIDFIELD
Action: PASS
Ada Boehm [LEFT_MIDFIELDER] made a weak pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made a superb attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Ada Boehm ──────────────────
Skill contribution: 93
Performance: 36
Total: 129
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 74
Total: 122

Clock: 83
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Mrs. Elwyn Wehner [CENTRE_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was there
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: 7
Assistance: 13
Total: 102
───────────────── Mrs. Elwyn Wehner ──────────────────
Skill contribution: 76
Performance: 0
Assistance: 7
Total: 83

Clock: 84
Area: LEFT_MIDFIELD
Action: SHOOT
Justice Armstrong [LEFT_WINGER] made an awful shot
Duel challenger: Lemuel Grant [GOALKEEPER] attempted an awesome save
Lemuel Grant saved the ball
SHOT Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 59
Performance: 14
Total: 73
───────────────── Lemuel Grant ──────────────────
Skill contribution: 74
Performance: 96
Total: 170

Clock: 85
Area: CENTRE_BACK
Action: PASS
Lemuel Grant [GOALKEEPER] made a poor pass to Miss Sandy Ernser [LEFT_MIDFIELDER]
Duel challenger: Kari Durgan [CENTRE_BACK] made a brilliant attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Lemuel Grant ──────────────────
Skill contribution: 79
Performance: 17
Total: 96
───────────────── Kari Durgan ──────────────────
Skill contribution: 91
Performance: 82
Total: 173

Clock: 86
Area: LEFT_MIDFIELD
Action: POSITION
Miss Sandy Ernser [LEFT_MIDFIELDER] tried with an awful effort to get free
Duel challenger: Patricia Sporer MD [RIGHT_MIDFIELDER] made an awful attempt to stay close
Miss Sandy Ernser won, he was in perfect position
POSITIONAL Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 5
Assistance: 11
Total: 106
───────────────── Patricia Sporer MD ──────────────────
Skill contribution: 62
Performance: 11
Assistance: 8
Total: 81

Clock: 87
Area: LEFT_MIDFIELD
Action: SHOOT
Miss Sandy Ernser [LEFT_MIDFIELDER] made a masterful shot
Duel challenger: Ned Padberg [GOALKEEPER] attempted a brilliant save
Miss Sandy Ernser scored a goal
SHOT Duel stats:
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 90
Performance: 106
Total: 196
───────────────── Ned Padberg ──────────────────
Skill contribution: 89
Performance: 90
Total: 179

Clock: 88
Area: LEFT_MIDFIELD
Action: PASS
Chyna Yundt [STRIKER] made an awful pass to Justice Armstrong [LEFT_WINGER]
Duel challenger: Miss Sandy Ernser [LEFT_MIDFIELDER] made an unbelievable attempt to intercept the ball
The pass was successful
PASSING Duel stats:
───────────────── Chyna Yundt ──────────────────
Skill contribution: 77
Performance: 6
Total: 83
───────────────── Miss Sandy Ernser ──────────────────
Skill contribution: 48
Performance: 116
Total: 164

Clock: 89
Area: LEFT_MIDFIELD
Action: POSITION
Justice Armstrong [LEFT_WINGER] tried with an awful effort to get free
Duel challenger: Brendon Hermann [RIGHT_MIDFIELDER] made an awful attempt to stay close
Justice Armstrong won, he was far
POSITIONAL Duel stats:
───────────────── Justice Armstrong ──────────────────
Skill contribution: 82
Performance: 9
Assistance: 11
Total: 102
───────────────── Brendon Hermann ──────────────────
Skill contribution: 73
Performance: -13
Assistance: 11
Total: 71

# Skill and ratings updates

Ratings before: home 2 - away 7
Ratings after: home -8 - away 17

Process finished with exit code 0
`
