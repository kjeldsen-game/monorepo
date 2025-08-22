import * as nextAuth from 'next-auth/react';

export const mockAuthenticatedSession = (overrides?: any) => {
  const mockedUseSession = nextAuth.useSession as jest.Mock;
  mockedUseSession.mockReturnValue({
    data: {
      user: { name: 'John Doe', email: 'john@example.com', ...overrides },
      accessToken: 'mock-token',
    },
    status: 'authenticated',
  });
};

export const mockUnauthenticatedSession = () => {
  const mockedUseSession = nextAuth.useSession as jest.Mock;
  mockedUseSession.mockReturnValue({
    data: null,
    status: 'unauthenticated',
  });
};
