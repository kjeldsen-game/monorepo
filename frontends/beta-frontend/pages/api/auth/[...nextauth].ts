import NextAuth from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import {
  AUTH_CLIENT_ID,
  AUTH_CLIENT_SECRET,
  API_AUTH_ENDPOINT,
} from '@/config';
import { signOut } from 'next-auth/react';

export default NextAuth({
  providers: [
    CredentialsProvider({
      name: 'credentials',
      credentials: {
        email: {
          label: 'email',
          type: 'text',
        },
        password: { label: 'password', type: 'password' },
      },
      type: 'credentials',
      authorize: async (credentials) => {
        const payload = {
          email: credentials?.email || '',
          password: credentials?.password || '',
          client_id: AUTH_CLIENT_ID,
          client_secret: AUTH_CLIENT_SECRET,
        };
        console.log(payload);
        try {
          const res = await fetch(`${API_AUTH_ENDPOINT}/auth/token`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(payload),
          });

          const data = await res.json();

          if (res.status !== 200) {
            throw new Error(data.message);
          }

          // console.log(data);
          // Save the access token as a cookie
          return { accessToken: data.accessToken };
        } catch (error) {
          console.error('Error during authorization:', error);
          throw error;
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
    // The JWT callback is used to store the access token
    jwt: async ({ token, user }) => {
      // If user object exists, it means it came from the authorize method
      if (user) {
        token.accessToken = user.accessToken;
      }

      return token;
    },

    // The session callback is used to fetch user data using the access token
    session: async ({ session, token }) => {
      // Add the access token to the session
      session.accessToken = token.accessToken;

      // Fetch user data from /auth/me using the access token
      if (session.accessToken) {
        try {
          const res = await fetch(`${API_AUTH_ENDPOINT}/auth/me`, {
            headers: {
              Authorization: `Bearer ${session.accessToken}`,
            },
          });

          if (res.status === 200) {
            const userData = await res.json();
            if (Object.keys(userData).length === 0) {
              console.log(
                'User session is empty, lets signout and clear the session!',
              );
              signOut();
              return { user: undefined, expires: '', accessToken: '' };
            }
            session.user = userData; // Attach the user data to the session
          } else {
            console.error('Failed to fetch user data:', res.status);
            signOut();
            return { user: undefined, expires: '', accessToken: '' };
          }
        } catch (error) {
          console.error('Error fetching user data:', error);
        }
      }

      return session;
    },
  },
  theme: {
    colorScheme: 'auto',
    brandColor: '',
    logo: '/logo.png',
  },
  debug: process.env.NODE_ENV === 'development',
});
