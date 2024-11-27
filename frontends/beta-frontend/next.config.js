const { i18n } = require('./next-i18next.config')

/** @type {import('next').NextConfig} */
const nextConfig = {
  // output: 'standalone',
  typescript: { // TODO tmp
    ignoreBuildErrors: true,
  },
  assetPrefix: process.env.NODE_ENV === 'production' ? 'http://kjeldsengame.com/' : 'http://localhost:3000',
  reactStrictMode: false,
  swcMinify: true,
  i18n,
  webpack(config) {
    // Add support for importing SVGs as React components
    config.module.rules.push({
      test: /\.svg$/,
      use: ['@svgr/webpack'],
    });

    return config;
  },
};

module.exports = nextConfig;
