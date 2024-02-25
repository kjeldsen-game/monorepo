import { useEffect, useState } from 'react'
import TeamView from '@/shared/components/TeamView'
import { CircularProgress } from '@mui/material'
import type { NextPage } from 'next'
import { useSession } from 'next-auth/react'
import { useTeamRepository } from '../api/team/useTeamRepository'
import { Player } from '@/shared/models/Player'
import { serverSideTranslations } from 'next-i18next/serverSideTranslations'

// eslint-disable-next-line react/prop-types
const Team: NextPage = () => {
  const { data: userData, status: sessionStatus } = useSession({ required: true })

  const { data, updateTeam } = useTeamRepository(userData?.user.teamId)

  const [teamPlayers, setTeamPlayers] = useState<Player[]>(data?.players ?? [])

  useEffect(() => {
    setTeamPlayers(data?.players ?? [])
  }, [data?.players])

  if (sessionStatus === 'loading' || !data) return <CircularProgress />

  const handlePlayerChange = (value: Player) => {
    if (data === undefined) return
    setTeamPlayers((prev) => {
      const index = prev.findIndex((p) => p.id === value.id)
      const newPlayers = [...prev]
      newPlayers[index] = { ...value }
      return newPlayers
    })
  }

  const handleTeamUpdate = () => {
    updateTeam(teamPlayers)
  }

  return (
    <>
      <TeamView
        isEditing={false}
        team={{ ...data, players: teamPlayers }}
        handlePlayerChange={handlePlayerChange}
        onTeamUpdate={handleTeamUpdate}></TeamView>
    </>
  )
}

export async function getStaticProps({ locale }: { locale: string }) {
  return {
    props: {
      ...(await serverSideTranslations(locale, ['common', 'game'])),
      // Will be passed to the page component as props
    },
  }
}

export default Team
