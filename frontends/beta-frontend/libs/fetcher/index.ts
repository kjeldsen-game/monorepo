import { API_GATEWAY_ENDPOINT } from '@/config'

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
    const res = await fetch(`${API_GATEWAY_ENDPOINT}${uri}`, { body, method, headers })
    try {
      return (await res.json()) as Result
    } catch (error) {
      return {} as Result
    }
  }

  return fetcher
}

//create connector function to connect REST API with fetch with error handling
export const connector = async (url: string, method: string, body?: any) => {
  const response = await fetch(`${API_GATEWAY_ENDPOINT}${url}`, {
    method: method,
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });
  if (response.ok) {
    const data = await response.json();
    return data;
  } else {
    const error = await response.json();
    throw new Error(error.message);
  }
};

export const fetcher = (url: string) => fetch(url).then((res) => res.json())
