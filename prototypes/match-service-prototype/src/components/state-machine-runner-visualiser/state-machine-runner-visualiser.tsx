import { createBrowserInspector } from '@statelyai/inspect'
import { useActor } from '@xstate/react'
import { FC } from 'react'
import { AnyActorLogic } from 'xstate'

const inspector = createBrowserInspector()

export const StateMachineRunnerVisualiser: FC<{ machine: AnyActorLogic }> = ({ machine }) => {
  const [snapShot, send, actorRef] = useActor(machine, { inspect: inspector.inspect })
  return (
    <div>
      {((actorRef.logic as any).events as Array<string>)
        .filter((x) => actorRef.getSnapshot().can({ type: x }))
        .map((x) => (
          <button
            key={x}
            onClick={() => {
              send({ type: x })
            }}>
            {x}
          </button>
        ))}
      <pre style={{ maxWidth: '80vw' }}>{JSON.stringify(snapShot, null, 2)}</pre>
    </div>
  )
}
