import { connector } from "@/libs/fetcher";
import { signIn } from "next-auth/react";

export async function apiSignup(username: string, password: string) {
  await connector(`/auth-service/auth/sign-up`, "POST", {
    username,
    password,
  });
  return apiSignIn(username, password);
}

export async function apiSignIn(username: any, password: any) {
  return signIn("credentials", {
    redirect: true,
    username: username,
    password: password,
    callbackUrl: "/",
  });
}
