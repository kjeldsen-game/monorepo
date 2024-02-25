'use client'

import { Team } from '@/shared/models/Team'

export interface LeagueGridProps {
  teams: Team[]
}

const LeagueGrid: React.FC<LeagueGridProps> = ({ teams }: LeagueGridProps) => {
  return <div>{teams.length}</div>
}

export { LeagueGrid }
