import { BookOpen, Globe, HelpCircle, Home, PenToolIcon } from 'lucide-react';

export const siteConfig = {
  title: 'grammr',
  description: 'Your go-to language learning reference!',
  url: 'https://grammr.app',
};

export const headerLinks = [
  { label: 'Home', href: '/', icon: Home },
  { label: 'Translate', href: '/translate', icon: Globe },
  { label: 'Flashcards', href: '/user/decks', icon: BookOpen },
  { label: 'Tools', href: '/tools', icon: PenToolIcon },
  { label: 'Help', href: '/help', icon: HelpCircle },
];
