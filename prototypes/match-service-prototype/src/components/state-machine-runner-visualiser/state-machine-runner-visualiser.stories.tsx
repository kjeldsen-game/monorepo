import type { Meta, StoryObj } from '@storybook/react'
import { matchMachine } from '@/state-machines/match-machine'
import { StateMachineRunnerVisualiser } from './state-machine-runner-visualiser'

const meta: Meta<typeof StateMachineRunnerVisualiser> = {
  component: StateMachineRunnerVisualiser,
}

export default meta
type Story = StoryObj<typeof StateMachineRunnerVisualiser>

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: () => <StateMachineRunnerVisualiser machine={matchMachine} />,
}
