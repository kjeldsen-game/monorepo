import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'
import { Match } from '@/shared/models/Match'

const API = '/match/'

const useAllPlayerMatchesRepository = (teamId?: string) => {
  const { data: allMatches } = useSWR<Match[]>([API, teamId], () => connectorAPI(API + `?page=${0}&size=${99}&teamId=${teamId}`, 'GET'))

  return { allMatches }
}
export { useAllPlayerMatchesRepository }
