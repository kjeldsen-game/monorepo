import useSWR from 'swr'
import { MatchReport } from '@/shared/models/MatchReport'
import { MatchReportResponse } from './models/MatchReportresponse'

const API = '/'

const useMatchReportRepository = (matchId: number) => {
  const { data, isLoading } = useSWR<MatchReport | undefined>(['match_mock'], () => fetch('/match_mock.json').then((response) => response.json()), {})

  return { report: data, isLoading }
}
export { useMatchReportRepository }
