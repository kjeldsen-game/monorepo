import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'

const API = '/team/'

const useTeamRepository = (team: string | undefined) => {
  const { data } = useSWR(team ? API + team : null, connectorAPI)

  return { data }
}
export { useTeamRepository }
