import { signIn } from 'next-auth/react'
import { API_GATEWAY_ENDPOINT } from '@/config'
import { fetcher } from '@/libs/fetcher'
import useSWR from 'swr'

export async function apiSignup(username: string, password: string) {
  const response = await fetch(`${API_GATEWAY_ENDPOINT}/auth-service/auth/sign-up`, {
    method: 'POST',
    body: JSON.stringify({ username, password }),
    headers: {
      'Content-Type': 'application/json',
    },
  })
  if (response.ok) {
    console.info('Account created successfully')
    apiSignIn(username, password)
  } else {
    console.error(response)
    // Append message to the DOM to show the user the error
    const error = await response.json()
    const errorElement = document.createElement('div')
    errorElement.innerHTML = `<div>${error.message}</div>`

    // Locate errorContainer and append errorElement to it
    const errorContainer = document.getElementById('errorContainer')
    errorContainer?.appendChild(errorElement)
  }
}

// I get Argument of type 'SignInFormValues' is not assignable to parameter of type 'string'.ts(2345) when I have username
// and password as strings. Will try to fix this tomorrow
export async function apiSignIn(username: any, password: any) {
  await signIn('credentials', {
    redirect: true,
    username: username,
    password: password,
    callbackUrl: '/',
  })
}

export function apiGetPlayers() {
  const { data, error, isLoading } = useSWR('http://localhost:8082/player?size=40&page=0', fetcher)
  if (error) return <div>failed to load</div>
  if (isLoading) return <div>loading...</div>

  console.log(data)
  // data.forEach((element: any) => console.log(element))

  return data
}
