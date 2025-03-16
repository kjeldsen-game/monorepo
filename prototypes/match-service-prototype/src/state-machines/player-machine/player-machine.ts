import { IPlayer } from '@/types'
import { setup } from 'xstate'

type PlayerMachineContext = {
  player: IPlayer
}

export const PlayerMachine = setup({
  types: {
    context: {} as PlayerMachineContext,
  },
}).createMachine({
  context: {
    player: {} as IPlayer,
  },
})
