import { API_HOST } from '@/config'

type Method = 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE'

interface RequestConfig<Data> {
  headers?: Record<string, string>
  method?: Method
  data?: Data
}

interface FetcherConfig {
  token?: string
}

export default function factory(c?: FetcherConfig) {
  const authHeaders = c?.token && { Authorization: `Bearer ${c!.token}` }

  async function fetcher<Result = unknown, Data = undefined>(uri: string, config?: RequestConfig<Data>): Promise<Result> {
    const method = config?.method
    const defaultHeaders = { 'Content-Type': 'application/json' }
    const headers = { ...defaultHeaders, ...config?.headers, ...authHeaders }

    const body = JSON.stringify(config?.data)
    const res = await fetch(`${API_HOST}${uri}`, { body, method, headers })
    try {
      return (await res.json()) as Result
    } catch (error) {
      return {} as Result
    }
  }

  return fetcher
}
