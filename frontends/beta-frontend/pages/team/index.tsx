import type { NextPage } from 'next'
import { Box } from '@mui/material'
import Grid from '@/shared/components/Grid/Grid'
import { SampleTeam } from '@/data/SampleTeam'
import TeamDetails from '@/shared/components/TeamDetails'
import PlayerTactics from '@/shared/components/PlayerTactics'
import TeamTactics from '@/shared/components/TeamTactics'
import { teamColumn } from '@/shared/components/Grid/TeamColumn'
import useSWR from "swr"

const fetcher = (url: string) => fetch(url).then((res) => res.json());
const API = "http://localhost:8082/player?size=40&page=0";

export async function getServerSideProps() {
  const repoInfo = await fetcher(API);
  return {
    props: {
      fallback: {
        [API]: repoInfo
      }
    }
  };
}

interface TeamProps {
  fallback: any;
}

const Team: NextPage<TeamProps> = ({fallback}) => {
  const { data, error } = useSWR(API);
  console.log(fallback[API])

  console.log("Is data ready?", !!data);
  console.log(data)

  if (error) return <div>failed to load</div>

  return (
    <>
      <Box>
        <Box sx={{ display: 'flex', marginBottom: '2rem', alignItems: 'center' }}>
          <TeamDetails {...SampleTeam} />
          <PlayerTactics />
          <TeamTactics />
        </Box>
        <Box sx={{ minWidth: '80vw' }}>
          <Grid rows={fallback[API]} columns={teamColumn} />
        </Box>
      </Box>
    </>
  )
}

export default Team
