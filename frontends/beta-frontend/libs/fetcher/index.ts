import { API_GATEWAY_ENDPOINT } from '@/config'
import { stringify } from 'querystring'

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

//create Auth Connector function to connect REST API with fetch with error handling
export const connector = async (url: string, method: string, body?: any) => {
  const response = await fetch(`${url}`, {
    method: method,
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  const contentType = response.headers.get("content-type");

  try {
    const data = await response.json();
    return data;
  }
  catch (e) {
    if (response.ok) {
      console.log("Response is ok but no JSON")
    } else {
      // TODO: try out catch e and throw new Error(e)
      const error = await response.json();
      throw new Error(error.message);
    }
  }

  return false;

  // if (response.ok && contentType?.includes("application/json")) {
  //   const data = await response.json();
  //   return data;
  // } else if (response.ok) {
  //   console.log("Response is ok but no JSON");
  // } else {
  //   const error = await response.json();
  //   throw new Error(error.message);
  // }
};

export const connectorAuth = (url: string, method: string, body?: any) => {
  return connector(`${API_GATEWAY_ENDPOINT}${url}`, method, body )
}

export const connectorAPI = (url: string, method: string, body?: any) => {
  return connector(`http://localhost:8082${url}`, method, body )
}
