import { useMatchReportRepository } from '@/pages/api/match/useMatchReportRepository'
import MatchReportContent from '@/shared/components/MatchReport/MatchReportContent'
import { MatchReportType } from '@/shared/models/MatchReport'
import { parseReport } from '@/shared/utils/MatchReportParser'
import { Box } from '@mui/material'
import type { NextPage } from 'next'
import { useSession } from 'next-auth/react'
import { serverSideTranslations } from 'next-i18next/serverSideTranslations'
import { useRouter } from 'next/router'
import { useEffect, useMemo, useState } from 'react'
import useSWR from 'swr'

// eslint-disable-next-line react/prop-types
const MatchReport: NextPage = () => {
  useSession({ required: true })

  const router = useRouter()

  const { report } = useMatchReportRepository(Number(router.query.id))

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'row',
        width: '100%',
        height: 'calc(100vh - 64px - 42px)',
      }}>
      <Box
        sx={{
          width: '10%',
        }}>
        Left panel
      </Box>
      {report ? (
        <MatchReportContent
          report={report}
          sx={{
            width: '80%',
          }}
        />
      ) : null}
      <Box
        sx={{
          width: '10%',
        }}>
        Right panel
      </Box>
    </Box>
  )
}

export async function getStaticPaths() {
  return {
    paths: ['/match/report/*'],
    fallback: true,
  }
}

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common', 'game'])),
      // Will be passed to the page component as props
    },
  }
}

export default MatchReport
