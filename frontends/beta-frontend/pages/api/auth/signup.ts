import { connectorAuth } from '@/libs/fetcher'
import { signIn } from 'next-auth/react'

export async function apiSignIn(username: string, password: string): Promise<number> {
  return new Promise((resolve, reject) => {
    signIn('credentials', {
      redirect: false,
      username: username,
      password: password,
    })
      .then((res) => {
        if (res?.status === 200) {
          resolve(res.status)
        }
        reject('Invalid credentials')
      })
      .catch((error) => {
        console.error(error)
        reject(error)
      })
  })
}

export async function apiSignup(username: string, password: string, teamName: string): Promise<string> {
  return new Promise((resolve, reject) => {
    connectorAuth('/auth/register', 'POST', {
      username,
      password,
      teamName,
    })
      .then((res) => {
        if (res.status === 200) {
          resolve(res.status)
        }
        reject('Register failed, status: ' + res.status + ' message: ' + res.message)
      })
      .catch((error) => {
        console.error(error)
        reject('Register failed')
      })
  })
}
