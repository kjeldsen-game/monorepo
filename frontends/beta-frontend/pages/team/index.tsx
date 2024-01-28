import TeamView from '@/shared/components/TeamView'
import { CircularProgress } from '@mui/material'
import type { NextPage } from 'next'
import { useSession } from 'next-auth/react'
import { connectorAPI } from '@/libs/fetcher'
import useSWR from 'swr'
import { useEffect } from 'react'

const API = '/team/9c2993c9-dc0b-4fb8-a4f5-0447724b1685'

export async function getServerSideProps() {
  const repoInfo = await connectorAPI(API)
  return {
    props: {
      fallback: {
        [API]: repoInfo,
      },
    },
  }
}

interface TeamProps {
  fallback: () => void
}

// eslint-disable-next-line react/prop-types
const Team: NextPage<TeamProps> = ({ fallback }) => {
  const { status: sessionStatus } = useSession()
  const { data } = useSWR(API, connectorAPI, { fallback })

  useEffect(() => {
    console.log(data)
  }, [data])
  if (sessionStatus === 'loading') return <CircularProgress />

  return (
    <>
      <TeamView team={data}></TeamView>
    </>
  )
}

export default Team
