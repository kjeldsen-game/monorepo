import NextAuth from 'next-auth'
import CredentialsProvider from 'next-auth/providers/credentials'
import { API_GATEWAY_ENDPOINT, AUTH_CLIENT_ID, AUTH_CLIENT_SECRET } from '@/config'
import factory from '@/libs/fetcher'

export default NextAuth({
  providers: [
    CredentialsProvider({
      id: 'credentials',
      name: 'my-project',
      credentials: {
        username: {
          label: 'username',
          type: 'text',
        },
        password: { label: 'Password', type: 'password' },
      },
      async authorize(credentials) {
        const payload = new URLSearchParams({
          username: credentials?.username || '',
          password: credentials?.password || '',
          grant_type: 'password',
          client_id: AUTH_CLIENT_ID,
          client_secret: AUTH_CLIENT_SECRET,
        })

        const res = await fetch(`${API_GATEWAY_ENDPOINT}/main-service/oauth/token`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
          },
          body: payload.toString(),
        })

        const data = await res.json()

        // Returning token to set in session
        return {
          token: data,
        }
      },
    }),
  ],
  secret: process.env.JWT_SECRET,
  pages: {
    signIn: '/signin',
  },
  session: { strategy: 'jwt' },
  callbacks: {
    jwt: async ({ token, user }) => {
      user && (token.user = user)
      return token
    },
    session: async ({ session, token }) => {
      session.user = token.user // Setting token in session
      return session
    },
  },
  theme: {
    colorScheme: 'auto', // "auto" | "dark" | "light"
    brandColor: '', // Hex color code #33FF5D
    logo: '/logo.png', // Absolute URL to image
  },
  // Enable debug messages in the console if you are having problems
  debug: process.env.NODE_ENV === 'development',
})
