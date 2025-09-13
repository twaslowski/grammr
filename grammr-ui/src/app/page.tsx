'use client';

import ToolCard from '@/components/tools/ToolCard';
import { Languages, MessageSquareIcon } from 'lucide-react';
import Image from 'next/image';

const tools = [
  {
    href: '/translate',
    icon: MessageSquareIcon,
    title: 'Translations',
    description: 'Translate sentences and get grammatical analysis',
  },
  {
    href: '/chat',
    icon: MessageSquareIcon,
    title: 'Chat',
    description: 'Learn by chatting with an AI tutor; translate and analyze phrases as needed!',
  },
  {
    href: '/tools/cyrtrans',
    icon: Languages,
    title: 'Cyrillic Transliteration',
    description: 'Convert between Cyrillic and Latin scripts for Russian text',
  },
];

const HomePage = () => {
  return (
    <main className='px-4 py-8 mx-auto'>
      <div className='mb-8 flex items-center'>
        <Image
          src='/images/mascot.png'
          alt='Grammr Mascot'
          width={96}
          height={96}
          className='mr-4 rounded-full shadow-md bg-white'
        />
        <div>
          <h1 className='text-4xl font-bold text-gray-900 mb-2'>Grammr: Language Learning Tools</h1>
          <p className='text-lg text-gray-700 max-w-2xl'>
            Grammr is your companion for language learning, offering a suite of tools for
            translation and grammatical analysis. Explore our features to enhance your study and
            understanding of languages.
          </p>
        </div>
      </div>
      <div className='grid gap-4 md:grid-cols-2 lg:grid-cols-3'>
        {tools.map((tool) => (
          <ToolCard
            key={tool.href}
            href={tool.href}
            icon={tool.icon}
            title={tool.title}
            description={tool.description}
          />
        ))}
      </div>
    </main>
  );
};

export default HomePage;
