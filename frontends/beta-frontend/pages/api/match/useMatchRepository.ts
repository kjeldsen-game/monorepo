import { useSWRConfig } from 'swr'
import { MatchCreationRequest } from './MatchCreationRequest'
import { connectorAPI } from '@/libs/fetcher'
import { Match } from '@/shared/models/Match'

const API = '/match/'

const useMatchRepository = (ownTeamId?: string) => {
  const { mutate } = useSWRConfig()

  const _patchMatch = async (matchId: string, params: Partial<Match>) => {
    connectorAPI<Partial<Match>>(API + matchId, 'PATCH', params).then(() => {
      mutate([API, ownTeamId])
    })
  }

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

  const acceptMatch = async (matchId: string) => {
    _patchMatch(matchId, { status: 'ACCEPTED' })
  }

  const declineMatch = async (matchId: string) => {
    _patchMatch(matchId, { status: 'REJECTED' })
  }

  return {
    createMatch,
    acceptMatch,
    declineMatch,
  }
}

export { useMatchRepository }
