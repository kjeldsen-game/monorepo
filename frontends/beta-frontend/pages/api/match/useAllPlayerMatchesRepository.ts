import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'
import { Match } from '@/shared/models/Match'

const API = '/match/'

const useAllPlayerMatchesRepository = (page: number = 0, size: number = 99, teamId?: string) => {
  const { data: allMatches } = useSWR<Match[]>([API, teamId], () => connectorAPI(API + `?page=${page}&size=${size}&teamId=${teamId}`, 'GET'))

  const pastMatches = allMatches?.filter((match) => new Date(match.dateTime).getTime() < new Date().getTime())
  const incomingMatches = allMatches?.filter((match) => new Date(match.dateTime).getTime() > new Date().getTime())
  const acceptedMatches = allMatches?.filter((match) => match.status === 'ACCEPTED')

  return { allMatches, pastMatches, incomingMatches, acceptedMatches }
}
export { useAllPlayerMatchesRepository }
