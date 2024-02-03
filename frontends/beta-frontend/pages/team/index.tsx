import TeamView from '@/shared/components/TeamView'
import { CircularProgress } from '@mui/material'
import type { NextPage } from 'next'
import { useSession } from 'next-auth/react'
import { useTeamRepository } from '../api/team/useTeamRepository'
import { Player } from '@/shared/models/Player'

// eslint-disable-next-line react/prop-types
const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({ required: true })

  const { data, updateTeamPlayer } = useTeamRepository(userData?.user.teamId)

  if (sessionStatus === 'loading') return <CircularProgress />

  const handlePlayerChange = (value: Player) => {
    if (data === undefined) return
    updateTeamPlayer(value)
  }

  return (
    <>
      <TeamView team={data} handlePlayerChange={handlePlayerChange}></TeamView>
    </>
  )
}

export default Team
