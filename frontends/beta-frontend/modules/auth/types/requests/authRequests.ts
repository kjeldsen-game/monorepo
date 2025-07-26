export interface PasswordResetRequest {
  token: string;
  newPassword: string;
  confirmPassword: string;
}

export interface ForgetPasswordRequest {
  email: string;
}

export interface SignUpRequest {
  email: string;
  teamName: string;
  password: string;
  confirmPassword: string;
}

export interface SignInRequest {
  email: string;
  password: string;
}
