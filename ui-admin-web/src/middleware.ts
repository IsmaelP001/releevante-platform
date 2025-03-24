import { NextRequest, NextResponse } from "next/server";
import createMiddleware from "next-intl/middleware";
import { routing } from "./config/i18n/routing";

// Create the middleware
const intlMiddleware = createMiddleware({
  locales: routing.locales,
  defaultLocale: routing.defaultLocale,
  localePrefix: 'always'
});

const PROTECTED_ROUTES = ["/reserved"];

export default async function middleware(request: NextRequest) {
  const pathname = request.nextUrl.pathname;
  
  // Handle authentication for protected routes
  if (PROTECTED_ROUTES.some(route => pathname.startsWith(route))) {
    const token = request.cookies.get(process.env.AUTH_COOKIE!)?.value;
    if (!token) {
      const redirectUrl = new URL(`/${routing.defaultLocale}/auth/code`, request.url);
      redirectUrl.searchParams.set("redirect", pathname);
      return NextResponse.redirect(redirectUrl);
    }
  }

  // Handle internationalization
  return intlMiddleware(request);
}

export const config = {
  // Match all pathnames except for
  // - files with extensions (e.g. /logo.png)
  // - internal Next.js paths (/api/, /_next/)
  // - favicon.ico
  matcher: ['/', '/((?!api|_next|.*\\..*).*)']
};
