import { getToken } from 'next-auth/jwt';
import { NextRequest, NextResponse } from 'next/server';

export async function middleware(req: NextRequest) {
  const { pathname } = req.nextUrl;

  const publicPaths = [
    '/auth/signin',
    '/auth/signup',
    '/api/auth/',
    '/favicon.ico',
    '/.well-known/',
    '/img/',
    '/_next/',
  ];

  if (publicPaths.some((path) => pathname.startsWith(path))) {
    return NextResponse.next();
  }

  const token = await getToken({
    req,
    secret: process.env.JWT_SECRET,
  });
  // console.log("Token found in middleware:", token);

  if (token === null) {
    // console.log('No token found, redirecting to sign-in page.');
    const loginUrl = new URL("/auth/signin", req.url);
    loginUrl.searchParams.set("callbackUrl", req.nextUrl.pathname);
    return NextResponse.redirect(loginUrl);
  }
}
