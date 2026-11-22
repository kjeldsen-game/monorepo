// Local demo-only shim: makes the whole app think it's logged in, so pages using
// useSession({ required: true }) render instead of redirecting to /auth/signin.
// Aliased in next.config.js in place of 'next-auth/react'.
export { SessionProvider, getSession, signOut } from 'next-auth/react';

export const fakeSession = {
  user: {
    id: '46442f0a-9de6-4062-88cb-35fc3e0da177',
    name: 'admin',
    teamId: '46442f0a-9de6-4062-88cb-35fc3e0da177',
  },
  accessToken: 'fake-local-token',
  expires: '2099-01-01T00:00:00.000Z',
};

export function useSession(_options?: unknown) {
  return { data: fakeSession, status: 'authenticated' as const, update: async () => fakeSession };
}

export async function signIn() {
  return undefined;
}
