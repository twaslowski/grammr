import { BookOpen, Globe, Home, Info, Phone } from 'lucide-react';

export const siteConfig = {
  title: 'grammr',
  description: 'Your go-to language learning reference!',
  url: 'https://grmmr.vercel.app',
};

export const headerLinks = [
  { label: 'Home', href: '/', icon: Home },
  { label: 'Translate', href: '/translate', icon: Globe },
  { label: 'Flashcards', href: '/user/decks', icon: BookOpen },
  { label: 'About', href: '/about', icon: Info },
  { label: 'Contact', href: '/contact', icon: Phone },
];
