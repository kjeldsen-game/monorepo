import { ITeam } from '@/types'
import { setup } from 'xstate'

type TacticContext = {
  team: ITeam
  tactic: string
}

export const TacticMachine = setup({
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
    transitionToTactic: {},
    inTactic: {},
  },
})

/**
 * Valid tactics and how to transition
 *
 *
 */
