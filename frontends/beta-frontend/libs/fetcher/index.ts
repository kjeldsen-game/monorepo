import { API_AUTH_ENDPOINT, API_ENDPOINT } from '@/config';
import { useError } from '@/shared/contexts/ErrorContext';

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
export const connector = async <T = any>(
  url: string,
  method: Method,
  body?: T,
  creds?: RequestCredentials,
  token?: string,
  headers?: Record<string, string>,
) => {
  if (body instanceof FormData) {
    for (let pair of body.entries()) {
      console.log(pair[0], pair[1]);
    }
  }

  const authHeaders = token && { Authorization: `Bearer ${token}` };

  const isForm = body instanceof FormData;

  const response = await fetch(`${url}`, {
    method: method,
    credentials: creds,
    headers: isForm
      ? { ...authHeaders }
      : {
          'Content-Type': 'application/json',
          ...authHeaders,
          ...(headers || {}),
        },
    body: isForm ? body : JSON.stringify(body),
  });

  if (!response.ok) {
    let errorMessage = '';
    try {
      const errorData = await response.json();
      errorMessage += `${errorData.message || JSON.stringify(errorData)}`;
    } catch (e) {
      console.error('Error parsing response JSON:', e);
      return;
    }
    console.error(errorMessage);
    throw new Error(errorMessage);
  }

  try {
    return await response.json();
  } catch (e) {
    console.log('Response is ok but no JSON');
    return response;
  }
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
  headers?: Record<string, string>,
) => {
  return connector(
    `${API_ENDPOINT}${url}`,
    method,
    body,
    creds,
    token,
    headers,
  );
};
