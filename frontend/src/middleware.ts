import { clerkMiddleware } from '@clerk/nextjs/server';
import { NextRequest, NextResponse } from 'next/server';

// Environment validation function
function validateEnvironment() {
  const required = [
    'BACKEND_HOST',
    'TTS_HOST',
    'NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY',
    'CLERK_SECRET_KEY',
  ];
  const missing = required.filter((key) => !process.env[key]);

  const urls = ['BACKEND_HOST', 'TTS_HOST'];
  urls.forEach((key) => {
    const value = process.env[key];
    if (value) {
      try {
        new URL(value);
      } catch {
        throw new Error('Invalid URL format for environment variable: ' + key);
      }
    }
  });

  if (missing.length > 0) {
    console.error(`Missing required environment variables: ${missing.join(', ')}`);
    throw new Error(`Missing required environment variables: ${missing.join(', ')}`);
  }
}

// Validate environment on middleware initialization
try {
  validateEnvironment();
} catch (error) {
  const errorMessage = error instanceof Error ? error.message : 'Unknown error';
  console.error('Environment validation failed:', errorMessage);
  process.exit(1);
}

const middleware = clerkMiddleware(async (auth, req: NextRequest) => {
  const { pathname } = req.nextUrl;

  // Handle API proxying at runtime
  if (pathname.startsWith('/api/v1/') || pathname.startsWith('/api/v2/')) {
    const backendHost = process.env.BACKEND_HOST;
    if (!backendHost) {
      return new NextResponse('Backend host not configured', { status: 500 });
    }

    const url = new URL(req.url);
    url.host = new URL(backendHost).host;
    url.protocol = new URL(backendHost).protocol;
    url.port = new URL(backendHost).port;

    return NextResponse.rewrite(url);
  }

  if (pathname === '/api/tts') {
    const ttsHost = process.env.TTS_HOST;
    if (!ttsHost) {
      return new NextResponse('TTS host not configured', { status: 500 });
    }

    const url = new URL(ttsHost);
    // Copy query parameters from original request
    const originalUrl = new URL(req.url);
    url.search = originalUrl.search;

    return NextResponse.rewrite(url);
  }

  // Continue with Clerk authentication for other routes
  return NextResponse.next();
});

export default middleware;

export const config = {
  matcher: [
    // Skip Next.js internals and all static files, unless found in search params
    '/((?!_next|[^?]*\\.(?:html?|css|js(?!on)|jpe?g|webp|png|gif|svg|ttf|woff2?|ico|csv|docx?|xlsx?|zip|webmanifest)).*)',
    // Always run for API routes
    '/(api|trpc)(.*)',
    '/user/(.*)',
  ],
};
