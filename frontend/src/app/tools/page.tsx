'use client';

import React from 'react';
import ToolCard from '@/components/tools/ToolCard';
import { Languages, MessageSquareIcon } from 'lucide-react';

const ToolsPage = () => {
  const tools = [
    {
      href: '/tools/cyrtrans',
      icon: Languages,
      title: 'Cyrillic Transliteration',
      description: 'Convert between Cyrillic and Latin scripts for Russian text',
    },
    {
      href: '/translate',
      icon: MessageSquareIcon,
      title: 'Translations',
      description: 'Translate sentences and get grammatical analysis',
    },
  ];

  return (
    <main className='px-4 py-8 mx-auto'>
      <div className='mb-8'>
        <h1 className='text-3xl font-bold text-gray-900 mb-2'>Language Tools</h1>
        <p className='text-lg text-gray-600'>
          Explore our collection of language learning and translation tools
        </p>
      </div>

      <div className='grid gap-6 md:grid-cols-2 lg:grid-cols-3'>
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

export default ToolsPage;
