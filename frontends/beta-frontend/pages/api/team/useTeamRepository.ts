import { connectorAPI } from '@/libs/fetcher'
import { Team } from '@/shared/models/Team'
import useSWR from 'swr'
import { TeamPlayerPatchRequest } from './TeamPlayerPatchRequest'
import { Player } from '@/shared/models/Player'

const API = '/team/'

const useTeamRepository = (team: string | undefined) => {
  const { data, mutate } = useSWR<Team>(team ? API + team : null, connectorAPI)

  const updateTeamPlayer = (value: Player): void => {
    if (!data) return
    const oldData = data.players
    const newData: TeamPlayerPatchRequest = {
      players: oldData.map((player) => {
        if (player.id === value.id) {
          return { id: value.id, position: value.position, status: value.status }
        }
        return { id: player.id, position: player.position, status: player.status }
      }),
    }

    if (!team) return
    connectorAPI<TeamPlayerPatchRequest>(API + team, 'PATCH', newData).then(() => {
      mutate()
    })
  }

  return { data, updateTeamPlayer }
}
export { useTeamRepository }
