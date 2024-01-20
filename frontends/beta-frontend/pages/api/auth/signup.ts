import { connector, connectorAuth } from '@/libs/fetcher'
import { signIn } from 'next-auth/react'

export async function apiSignup(username: string, password: string, teamName: string) {
  await connectorAuth('/auth/sign-up', 'POST', {
    username,
    password,
    teamName,
  })
  return apiSignIn(username, password)
}

export async function apiSignIn(username: any, password: any) {
  return signIn('credentials', {
    redirect: true,
    username: username,
    password: password,
    callbackUrl: '/',
  })
}
