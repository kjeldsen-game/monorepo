import NextAuth from 'next-auth'
import CredentialsProvider from 'next-auth/providers/credentials'
import { API_ENDPOINT, AUTH_CLIENT_ID, AUTH_CLIENT_SECRET } from '@/config'

export default NextAuth({
  providers: [
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        username: {
          label: 'email',
          type: 'text',
        },
        password: { label: 'password', type: 'password' },
      },
      type: 'credentials',
      authorize: async (credentials) => {
        const payload = {
          email: credentials?.username || '',
          password: credentials?.password || '',
          client_id: AUTH_CLIENT_ID,
          client_secret: AUTH_CLIENT_SECRET,
        }

        const res = await fetch(`${API_ENDPOINT}/auth/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(payload),
        })

        if (res.status !== 200) throw new Error('Invalid credentials')

        // Returning token to set in session
        const data = await res.json()
        return data
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
      session.user = token.user

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
