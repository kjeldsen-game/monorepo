import { fetcher } from '@/libs/fetcher'
import useSWR from 'swr'
import { connector } from '@/libs/fetcher'

export function getPlayers() {
  const { data, error, isLoading } = useSWR('http://localhost:8082/player?size=40&page=0', fetcher)
  if (error) return <div>failed to load</div>
  if (isLoading) return <div>loading...</div>

  // console.log(typeof data)
  // console.log(data)
  // data.forEach((element: any) => console.log(element))

  return data
}
