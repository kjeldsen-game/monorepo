import { API_HOST } from '@/config'

type Method = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'

interface RequestConfig<Data> {
  token?: string
  headers?: Record<string, string>
  method?: Method
  data?: Data
}

export default async function fetcher<Result = unknown, Data = undefined>(uri: string, config?: RequestConfig<Data>): Promise<Result | undefined> {
  const method = config?.method
  const authHeaders = config?.token && { Authorization: `Bearer ${config!.token}` }
  const defaultHeaders = { 'Content-Type': 'application/json' }
  const headers = { ...defaultHeaders, ...config?.headers, ...authHeaders }

  const body = JSON.stringify(config?.data)
  const res = await fetch(`${API_HOST}${uri}`, { body, method, headers })
  try {
    return (await res.json()) as Result
  } catch (error) {
    return undefined
  }
}
