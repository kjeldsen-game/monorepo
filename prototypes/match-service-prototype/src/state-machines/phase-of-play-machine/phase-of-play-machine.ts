import { ITeam } from '@/types'
import { setup } from 'xstate'

type TacticContext = {
  team: ITeam
  tactic: string
}

export const PhaseOfPlayMachine = setup({
  types: {
    context: {} as
      | TacticContext
      | {
          /** Empty */
        },
  },
}).createMachine({
  id: 'tactic-machine',
  states: {
    InPossession: {},
    OutOfPossession: {},
    TransitionToAttacking: {},
    TransitionToDefending: {},
  },
})

/**
 * Valid tactics and how to transition
 *
 *
 */
