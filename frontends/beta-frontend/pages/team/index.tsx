import TeamView from '@/shared/components/TeamView'
import { CircularProgress } from '@mui/material'
import type { NextPage } from 'next'
import { useSession } from 'next-auth/react'
import { useTeam } from './useTeam'

// eslint-disable-next-line react/prop-types
const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({ required: true })

  console.log(userData?.user.teamId)
  const { data } = useTeam(userData?.user.teamId)

  if (sessionStatus === 'loading') return <CircularProgress />

  return (
    <>
      <TeamView team={data}></TeamView>
    </>
  )
}

export default Team
