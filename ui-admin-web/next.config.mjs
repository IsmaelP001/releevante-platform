import createNextIntlPlugin from 'next-intl/plugin';

const withNextIntl = createNextIntlPlugin('./src/config/i18n/request.ts');
 
/** @type {import('next').NextConfig} */
const nextConfig = {
  output: "standalone",
  eslint: {
    ignoreDuringBuilds: true,
  },
  typescript: {
    ignoreBuildErrors: true
  },
  images: {
    remotePatterns: [
      {
        protocol: 'https',
        hostname: '**',
      },
      {
        protocol: 'http',
        hostname: '**',
      }
    ],
  },
  // Add trailingSlash configuration
  trailingSlash: false,
  // Add poweredByHeader configuration
  poweredByHeader: false,
  // Improve production performance
  swcMinify: true,
  reactStrictMode: true,
};

export default withNextIntl(nextConfig);
