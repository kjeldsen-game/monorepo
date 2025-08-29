import { SignUpRequest } from 'modules/auth/types/requests/authRequests';
import { resetPassword, forgetPassword, signUp } from '../authApi';
import { connectorAuth } from '@/libs/fetcher';

jest.mock('@/libs/fetcher');

describe('Auth API', () => {
  beforeEach(() => {
    (connectorAuth as jest.Mock).mockClear();
  });

  it('calls connectorAuth for resetPassword', async () => {
    const request = {
      token: 'abc',
      newPassword: '123',
      confirmPassword: '123',
    };
    await resetPassword(request);
    expect(connectorAuth).toHaveBeenCalledWith(
      '/auth/reset-password',
      'POST',
      request,
    );
  });

  it('calls connectorAuth for forgetPassword', async () => {
    const request = { email: 'test@example.com' };
    const result = await forgetPassword(request);
    expect(connectorAuth).toHaveBeenCalledWith(
      '/auth/forget-password',
      'POST',
      request,
    );
  });

  it('calls connectorAuth for signUp', async () => {
    const request: SignUpRequest = {
      email: 'test@example.com',
      password: '123',
      teamName: 'John',
      confirmPassword: '123',
    };
    const result = await signUp(request);
    expect(connectorAuth).toHaveBeenCalledWith(
      '/auth/register',
      'POST',
      request,
    );
  });
});
