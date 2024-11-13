import { API_ENDPOINT } from '@/config';

type Method =
  | 'CONNECT'
  | 'DELETE'
  | 'GET'
  | 'HEAD'
  | 'OPTIONS'
  | 'PATCH'
  | 'POST'
  | 'PUT'
  | 'TRACE';

interface RequestConfig<Data> {
  headers?: Record<string, string>;
  method?: Method;
  data?: Data;
}

interface FetcherConfig {
  token?: string;
}

export default function factory(c?: FetcherConfig) {
  const authHeaders = c?.token && { Authorization: `Bearer ${c!.token}` };

  async function fetcher<Result = unknown, Data = undefined>(
    uri: string,
    config?: RequestConfig<Data>,
  ): Promise<Result> {
    const method = config?.method;
    const defaultHeaders = { 'Content-Type': 'application/json' };

    const headers = { ...defaultHeaders, ...config?.headers, ...authHeaders };

    const body = JSON.stringify(config?.data);
    const res = await fetch(`${API_ENDPOINT}${uri}`, { body, method, headers });
    try {
      return (await res.json()) as Result;
    } catch (error) {
      return {} as Result;
    }
  }

  return fetcher;
}

//create Auth Connector function to connect REST API with fetch with error handling
export const connector = async (
  url: string,
  method: Method,
  body?: unknown,
  creds?: RequestCredentials,
  token?: string,
) => {
  const response = await fetch(`${url}`, {
    method: method,
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify(body),
  });

  try {
    const data = await response.json();
    return data;
  } catch (e) {
    if (response.ok) {
      console.log('Response is ok but no JSON');
    }
  }
  return false;
};

export const connectorAuth = (url: string, method: Method, body?: unknown) => {
  return connector(`${API_ENDPOINT}${url}`, method, body);
};

export const connectorAPI = <T>(
  url: string,
  method: Method = 'GET',
  body?: T,
  creds?: RequestCredentials,
  token?: string,
) => {
  return connector(`${API_ENDPOINT}${url}`, method, body, creds, token);
};
