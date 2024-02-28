import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'
import { Team } from '@/shared/models/Team'

const API = '/team/'

const useAllTeamsRepository = (page: number, size: number) => {
  const { data: allTeams } = useSWR<Team[]>([API, page, size], () => connectorAPI(API + `?page=${page}&size=${size}`, 'GET'))

  return { allTeams }
}
export { useAllTeamsRepository }
