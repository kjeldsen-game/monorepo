import { API_HOST } from '@/config'

type Method = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'
type RequestConfig<Data> = { headers?: Record<string, string>, method?: Method, data?: Data }

export default async function fetcher<JSON = any, Data = undefined>(
  uri: string,
  config?: RequestConfig<Data>
): Promise<JSON> {
  const method = config?.method
  const authHeaders = { 'Authorization': 'Bearer foo' }
  const defaultHeaders = { 'Content-Type': 'application/json' }
  const headers = { ...defaultHeaders, ...(config?.headers), ...authHeaders }

  const body = JSON.stringify(config?.data)
  const res = await fetch(`${API_HOST}${uri}`, { body, method, headers })
  return await res.json() as JSON
}