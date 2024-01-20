import { connectorAuth } from '@/libs/fetcher'
import { signIn } from 'next-auth/react'

export async function apiSignIn(username: string, password: string) {
  return signIn("credentials", {
    redirect: true,
    username: username,
    password: password,
    callbackUrl: "/",
  });
}


export async function apiSignup(username: string, password: string, teamName: string) {
  await connectorAuth('/auth/sign-up', 'POST', {
    username,
    password,
    teamName,
  })
  return apiSignIn(username, password)
}

