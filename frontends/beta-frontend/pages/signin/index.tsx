import React from 'react'
import { Box, Button, Card, CardContent, CardHeader, Link as MuiLink, TextField } from '@mui/material'
import { Controller, useForm } from 'react-hook-form'
import { signIn } from 'next-auth/react'
import Head from 'next/head'
import Link from 'next/link'
import { NextPageWithLayout } from '@/pages/_app'
import { CenterContainer } from '@/shared/layout'

interface SignInFormValues {
  username: string
  password: string
}

const SignInPage: NextPageWithLayout = () => {
  const { handleSubmit, control } = useForm<SignInFormValues>({
    mode: 'onBlur',
    reValidateMode: 'onChange',
  })

  return (
    <>
      <Head>
        <title>Sign In</title>
      </Head>
      <Card>
        <CardHeader title="Sign In" component={Box} />
        <CardContent>
          <form
            css={{
              display: 'flex',
              flexDirection: 'column',
              alignContent: 'center',
              justifyContent: 'center',
              rowGap: '1rem',
            }}
            onSubmit={handleSubmit(async (data) => {
              await signIn('credentials', {
                redirect: true,
                username: data.username,
                password: data.password,
                callbackUrl: '/',
              })
            })}>
            <Controller
              name="username"
              control={control}
              defaultValue=""
              rules={{
                required: 'Username required',
              }}
              render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
                <TextField
                  label="Username"
                  variant="filled"
                  value={value}
                  onChange={onChange}
                  onBlur={onBlur}
                  error={!!error}
                  helperText={error ? error.message : null}
                  type="text"
                />
              )}
            />
            <Controller
              name="password"
              control={control}
              defaultValue=""
              rules={{ required: 'Password required' }}
              render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
                <TextField
                  label="Password"
                  variant="filled"
                  value={value}
                  onChange={onChange}
                  onBlur={onBlur}
                  error={!!error}
                  helperText={error ? error.message : null}
                  type="password"
                />
              )}
            />
            <div>
              <Button type="submit" variant="contained" color="primary">
                Login
              </Button>
            </div>
          </form>
          <Link href="/signup" passHref>
            <MuiLink>Create an account</MuiLink>
          </Link>
        </CardContent>
      </Card>
    </>
  )
}

SignInPage.getLayout = (page) => <CenterContainer>{page}</CenterContainer>

export default SignInPage
