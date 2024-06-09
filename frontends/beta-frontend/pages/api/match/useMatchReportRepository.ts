import useSWR from 'swr'
import { parseReport } from '../../../shared/utils/MatchReportParser'
import { MatchReportType } from '@/shared/models/MatchReport'

const API = '/'

const useMatchReportRepository = (matchId: number) => {
  const { data, isLoading } = useSWR<MatchReportType | undefined>(
    ['match_mock'],
    () => fetch('/match_mock.md').then((response) => response.text().then((text) => parseReport(text))),
    {},
  )

  return { report: data, isLoading }
}
export { useMatchReportRepository }
