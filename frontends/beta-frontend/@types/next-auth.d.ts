import { GameUser } from '@/shared/models/GameUser';

declare module 'next-auth' {
  /**
   * Returned by `useSession`, `getSession` and received as a prop on the `SessionProvider` React Context
   */
  interface Session {
    user: GameUser;
    accessToken: string;
  }
}

declare module 'next-auth/jwt' {
  interface JWT {
    user: User;
  }
}
