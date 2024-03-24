import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'
import { Match } from '@/shared/models/Match'

const API = '/match/'

const useAllPlayerMatchesRepository = (page: number = 0, size: number = 99, teamId?: string) => {
  const { data: allMatches } = useSWR<Match[]>([API, teamId], () => connectorAPI(API + `?page=${page}&size=${size}&teamId=${teamId}`, 'GET'))

  return { allMatches }
}
export { useAllPlayerMatchesRepository }
