import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'

const API = '/team/'

const useTeam = (team: string | undefined) => {
  const { data } = useSWR(team ? API + team : null, connectorAPI)

  return { data }
}
export { useTeam }
