import { useSWRConfig } from 'swr'
import { MatchCreationRequest } from './MatchCreationRequest'
import { connectorAPI } from '@/libs/fetcher'

const API = '/match/'

const useMatchRepository = (ownTeamId?: string) => {
  const { mutate } = useSWRConfig()

  const createMatch = async (rivalTeamId: string, date: Date) => {
    if (!ownTeamId) return
    const newData: MatchCreationRequest = {
      home: {
        id: ownTeamId,
        modifiers: {
          tactic: 'DOUBLE_TEAM',
          horizontalPressure: 'SWARM_CENTRE',
          verticalPressure: 'MID_PRESSURE',
        },
      },
      away: {
        id: rivalTeamId,
        modifiers: {
          tactic: 'DOUBLE_TEAM',
          horizontalPressure: 'SWARM_CENTRE',
          verticalPressure: 'MID_PRESSURE',
        },
      },
      dateTime: date,
    }
    connectorAPI<MatchCreationRequest>(API, 'POST', newData).then(() => {
      mutate([API, ownTeamId])
    })
  }

  return {
    createMatch,
  }
}

export { useMatchRepository }
