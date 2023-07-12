import React from 'react'
import { Box, Button, Card, CardContent, CardHeader, TextField } from '@mui/material'
import { Controller, useForm } from 'react-hook-form'
import { CenterContainer } from '@/shared/layout'
import { NextPageWithLayout } from '@/pages/_app'
import { API_GATEWAY_ENDPOINT } from '@/config'
import { fetcher } from '@/libs/fetcher'

interface SignUpFormValues {
  username: string
  password: string
  confirmPassword: string
}

const SignUpPage: NextPageWithLayout = () => {
  const { handleSubmit, control, watch } = useForm<SignUpFormValues>({
    mode: 'onBlur',
    reValidateMode: 'onChange',
  })

  return (
    <Card>
      <CardHeader title="Create an account" component={Box} />
      <CardContent>
        <form
          css={{
            display: 'flex',
            flexDirection: 'column',
            alignContent: 'center',
            justifyContent: 'center',
            rowGap: '1rem',
          }}
          onSubmit={handleSubmit(async ({ username, password }) => {
            // const usernames = JSON.stringify(username)
            // const passwords = JSON.stringify(password)
            // fetch('http://localhost:8081/auth/sign-up', { method: 'POST', body: { username, password } })
          })}>
          <Controller
            name="username"
            control={control}
            defaultValue=""
            rules={{ required: 'Username required' }}
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
              <TextField
                label="Username"
                variant="filled"
                value={value}
                onChange={onChange}
                onBlur={onBlur}
                error={!!error}
                helperText={error ? error.message : null}
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
          <Controller
            name="confirmPassword"
            control={control}
            defaultValue=""
            rules={{
              required: 'Password required',
              validate: (val: string) => {
                if (watch('password') != val) {
                  return 'Your passwords do no match'
                }
              },
            }}
            render={({ field: { onChange, onBlur, value }, fieldState: { error } }) => (
              <TextField
                label="Confirm Password"
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
              Sign Up
            </Button>
          </div>
        </form>
      </CardContent>
    </Card>
  )
}

SignUpPage.getLayout = (page) => <CenterContainer>{page}</CenterContainer>

export default SignUpPage
