import type { PLAY_DIRECTION, NormalizedPostion, Position } from '@/types'

/*
  DIRECTION FORWARDS (Player Perspective)
  .   LB   LM   LF  .
  GD  CB   CM   CF  GA
  .   RB   RM   RF  .
  NORMALIZED POSITION
  [0,0] [1,0] [2,0] [3,0] [4,0] 
  [0,1] [1,1] [2,1] [3,1] [4,1]
  [0,2] [1,2] [2,2] [3,2] [4,2]
*/
export const forwardMap = new Map<Position, NormalizedPostion>([
  ['GOALS', [0, 1]],
  ['CENTER-BACK', [1, 1]],
  ['CENTER-MID', [2, 1]],
  ['CENTER-FORWARD', [3, 1]],
  ['LEFT-BACK', [1, 0]],
  ['LEFT-MID', [2, 0]],
  ['LEFT-FORWARD', [3, 0]],
  ['RIGHT-BACK', [1, 2]],
  ['RIGHT-MID', [2, 2]],
  ['RIGHT-FORWARD', [3, 2]],
])
export const forwardNormalizedMap = new Map<NormalizedPostion, Position>([
  [[0, 1], 'GOALS'],
  [[1, 1], 'CENTER-BACK'],
  [[2, 1], 'CENTER-MID'],
  [[3, 1], 'CENTER-FORWARD'],
  [[1, 0], 'LEFT-BACK'],
  [[2, 0], 'LEFT-MID'],
  [[3, 0], 'LEFT-FORWARD'],
  [[1, 2], 'RIGHT-BACK'],
  [[2, 2], 'RIGHT-MID'],
  [[3, 2], 'RIGHT-FORWARD'],
])

/*
    DIRECTION BACKWARDS (Competitor Perspective)
    .   RF   RM   RB  .
    GA  CF   CM   CB  GD
    .   LF   LM   LB  .
    NORMALIZED POSITION
    [0,0] [1,0] [2,0] [3,0] [4,0] 
    [0,1] [1,1] [2,1] [3,1] [4,1]
    [0,2] [1,2] [2,2] [3,2] [4,2]
  */
export const BackwardsMap = new Map<Position, NormalizedPostion>([
  ['GOALS', [4, 1]],
  ['CENTER-BACK', [3, 1]],
  ['CENTER-MID', [2, 1]],
  ['CENTER-FORWARD', [1, 1]],
  ['RIGHT-BACK', [3, 0]],
  ['RIGHT-MID', [2, 0]],
  ['RIGHT-FORWARD', [1, 0]],
  ['LEFT-BACK', [3, 2]],
  ['LEFT-MID', [2, 2]],
  ['LEFT-FORWARD', [1, 2]],
])
export const backwardsNormalizeMap = new Map<NormalizedPostion, string>([
  [[4, 1], 'GOALS'],
  [[3, 1], 'CENTER-BACK'],
  [[2, 1], 'CENTER-MID'],
  [[1, 1], 'CENTER-FORWARD'],
  [[3, 0], 'RIGHT-BACK'],
  [[2, 0], 'RIGHT-MID'],
  [[1, 0], 'RIGHT-FORWARD'],
  [[3, 2], 'LEFT-BACK'],
  [[2, 2], 'LEFT-MID'],
  [[1, 2], 'LEFT-FORWARD'],
])

export const normalizedPostion = (position: Position, playDirection: PLAY_DIRECTION) => {
  switch (playDirection) {
    case 'FORWARDS': {
      return forwardMap.get(position)
    }
    case 'BACKWARDS': {
      return BackwardsMap.get(position)
    }
  }
}

export const normalizedPostionToDescriptivePostion = (position: NormalizedPostion, playDirection: PLAY_DIRECTION) => {
  switch (playDirection) {
    case 'FORWARDS': {
      return forwardNormalizedMap.get(position)
    }
    case 'BACKWARDS': {
      return backwardsNormalizeMap.get(position)
    }
  }
}
