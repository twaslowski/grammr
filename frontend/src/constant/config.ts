import { BookOpen, Globe, HelpCircle, Home, MessageCircleMore } from 'lucide-react';

export const siteConfig = {
  title: 'grammr',
  description: 'Your go-to language learning reference!',
  url: 'https://grammr.app',
};

export const headerLinks = [
  { label: 'Home', href: '/', icon: Home },
  { label: 'Translate', href: '/translate', icon: Globe },
  { label: 'Chat', href: '/chat', icon: MessageCircleMore },
  { label: 'Flashcards', href: '/user/decks', icon: BookOpen },
  { label: 'Help', href: '/help', icon: HelpCircle },
];
