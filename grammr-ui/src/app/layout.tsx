import '@/styles/globals.css';

import { ClerkProvider } from '@clerk/nextjs';
import { SpeedInsights } from '@vercel/speed-insights/next';
import { Metadata } from 'next';
import * as React from 'react';

import { siteConfig } from '@/constant/config';
import { LanguageProvider } from '@/context/LanguageContext';
import { Toaster } from '@/components/ui/toaster';
import { useIsMobile } from '@/hooks/use-mobile';
import { Header } from '@/components/common/Header';
import { TokenPopoverProvider } from '@/context/TokenPopoverContext';
import {ChatProvider} from "@/context/ChatContext";

export const metadata: Metadata = {
  metadataBase: new URL(siteConfig.url),
  title: {
    default: siteConfig.title,
    template: `%s | ${siteConfig.title}`,
  },
  description: siteConfig.description,
  robots: { index: true, follow: true },
  // !STARTERCONF this is the default favicon, you can generate your own from https://realfavicongenerator.net/
  // ! copy to /favicon folder
  icons: {
    icon: '/favicon/favicon.ico',
    shortcut: '/favicon/favicon-96x96.png',
    apple: '/favicon/apple-touch-icon.png',
  },
  manifest: `/favicon/site.webmanifest`,
  openGraph: {
    url: siteConfig.url,
    title: siteConfig.title,
    description: siteConfig.description,
    siteName: siteConfig.title,
    images: [`${siteConfig.url}/images/logo.jpg`],
    type: 'website',
    locale: 'en_US',
  },
  authors: [
    {
      name: 'Tobias Waslowski',
      url: 'https://twaslowski.com',
    },
  ],
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <ClerkProvider>
      <html>
        <body className='flex flex-col min-h-screen'>
          <LanguageProvider>
            <TokenPopoverProvider>
              <ChatProvider>
                <Header />
                <main className='flex-grow'>{children}</main>
              </ChatProvider>
            </TokenPopoverProvider>
          </LanguageProvider>
          <Toaster />
          <SpeedInsights />
        </body>
      </html>
    </ClerkProvider>
  );
}
