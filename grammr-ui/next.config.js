/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      {
        source: '/api/v1/:path*',
        destination: `${process.env.BACKEND_HOST}/api/v1/:path*`,
      },
      {
        source: '/api/v2/:path*',
        destination: `${process.env.BACKEND_HOST}/api/v2/:path*`,
      },
      {
        source: '/api/tts',
        destination: `${process.env.TTS_HOST}`,
      },
    ];
  },
  eslint: {
    dirs: ['src'],
  },

  reactStrictMode: true,

  images: {
    remotePatterns: [new URL('https://img.clerk.com/**')],
  },

  webpack(config) {
    // Grab the existing rule that handles SVG imports
    const fileLoaderRule = config.module.rules.find((rule) =>
      rule.test?.test?.('.svg'),
    );

    config.module.rules.push(
      // Reapply the existing rule, but only for svg imports ending in ?url
      {
        ...fileLoaderRule,
        test: /\.svg$/i,
        resourceQuery: /url/, // *.svg?url
      },
      // Convert all other *.svg imports to React components
      {
        test: /\.svg$/i,
        issuer: { not: /\.(css|scss|sass)$/ },
        resourceQuery: { not: /url/ }, // exclude if *.svg?url
        loader: '@svgr/webpack',
        options: {
          dimensions: false,
          titleProp: true,
        },
      },
    );

    // Modify the file loader rule to ignore *.svg, since we have it handled now.
    fileLoaderRule.exclude = /\.svg$/i;

    return config;
  },

  env: {
    BACKEND_HOST: process.env.BACKEND_HOST,
    TTS_HOST: process.env.TTS_HOST,
    NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY:
      process.env.NEXT_PUBLIC_CLERK_PUBLISHABLE_KEY,
    CLERK_SECRET_KEY: process.env.CLERK_SECRET_KEY,
  },
};

module.exports = nextConfig;
