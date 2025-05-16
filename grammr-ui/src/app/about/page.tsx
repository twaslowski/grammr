'use client';

import { Github, LinkedinIcon, NotebookPen } from 'lucide-react';
import Link from 'next/link';
import React, { useState } from 'react';

import LanguageFeatureTable from '@/components/table/LanguageTable';
import { languageFeatures } from '@/constant/languages';

export default function Page() {
  // You can add state management here if needed
  const [activeTab, setActiveTab] = useState('languages');

  return (
    <div className='container mx-auto px-12'>
      <section className='mb-12 text-center py-8'>
        <h1 className='text-4xl font-bold mb-4'>About Grammr</h1>
      </section>

      <div className='flex border-b mb-8'>
        <button
          className={`py-2 px-4 ${
            activeTab === 'languages' ? 'border-b-2 border-blue-500 font-medium' : ''
          }`}
          onClick={() => setActiveTab('languages')}
        >
          Supported Languages
        </button>
        <button
          className={`py-2 px-4 ${
            activeTab === 'service' ? 'border-b-2 border-blue-500 font-medium' : ''
          }`}
          onClick={() => setActiveTab('service')}
        >
          The Service
        </button>
        <button
          className={`py-2 px-4 ${
            activeTab === 'open-source' ? 'border-b-2 border-blue-500 font-medium' : ''
          }`}
          onClick={() => setActiveTab('open-source')}
        >
          Open Source
        </button>
        <button
          className={`py-2 px-4 ${
            activeTab === 'author' ? 'border-b-2 border-blue-500 font-medium' : ''
          }`}
          onClick={() => setActiveTab('author')}
        >
          The Author
        </button>
      </div>

      <div className='mb-12'>
        {activeTab === 'languages' && (
          <div>
            <LanguageFeatureTable languageFeatures={languageFeatures} />
          </div>
        )}
        {activeTab === 'service' && (
          <div>
            <h2 className='text-2xl font-bold mb-4'>On Grammr</h2>
            <p className='mb-4'>
              Grammr is designed to be a reference toolkit for grammar and vocabulary. It supports a
              variety of languages and is built with a focus on accessibility and ease of use.
              Grammr enables its users to translate, practice, and learn new languages with
              confidence by providing information on phrases, named entities and cultural
              references, grammar, rules, vocabulary, and more.
            </p>
            <p className='mb-4'>
              In order to provide the best possible experience, people with different levels of
              language proficiency and programming experience are encouraged to contribute to the
              project. If you're interested, feel free to read more in the Open Source section or
              reach out directly.
            </p>
          </div>
        )}

        {activeTab === 'open-source' && (
          <div>
            <h2 className='text-2xl font-bold mb-4'>Open Source</h2>
            <p className='mb-4'></p>
            <p className='mb-4'>
              The backend powering this website, grammr-core, is licensed under the Apache 2.0
              License.
            </p>
          </div>
        )}

        {activeTab === 'author' && (
          <div>
            <h2 className='text-2xl font-bold mb-4'>On the author</h2>
            <p className='mb-6'>
              Tobias is a software engineer, open source enthusiast and language learner based in
              Hamburg, Germany.
            </p>

            <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6'>
              <div className='bg-gray-50 rounded-lg p-6'>
                <div className='h-32 w-32 bg-gray-200 rounded-full mx-auto mb-4 flex items-center justify-center'>
                  <p className='text-gray-500 text-sm'>Photo</p>
                </div>
                <h3 className='text-lg font-medium text-center'>Tobias Waslowski</h3>
                <div className='flex items-center justify-center gap-4'>
                  <a
                    href='https://github.com/twaslowski/'
                    target='_blank'
                    rel='noopener noreferrer'
                    className='text-gray-600 hover:text-black'
                    aria-label='GitHub Repository'
                  >
                    <Github size={18} />
                  </a>
                  <a
                    href='https://linkedin.com/twaslowski'
                    target='_blank'
                    rel='noopener noreferrer'
                    className='text-gray-600 hover:text-black'
                    aria-label='LinkedIn Profile'
                  >
                    <LinkedinIcon size={18} />
                  </a>
                  <a
                    href='https://blog.twaslowski.com'
                    target='_blank'
                    rel='noopener noreferrer'
                    className='text-gray-600 hover:text-black'
                    aria-label='Blog'
                  >
                    <NotebookPen size={18} />
                  </a>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>

      {/* Call to Action Section */}
      <section className='bg-gray-100 p-8 rounded-lg text-center'>
        <h2 className='text-2xl font-bold mb-4'>Contribute</h2>
        <p className='mb-6'>
          If you are interested in contributing to the project, don't hesitate to reach out. Whether
          you are knowledgeable about NLP, Language Learning, Programming, or would like to help in
          a different way, any support is appreciated!
        </p>
        <Link
          href='/contact'
          className='bg-blue-500 hover:bg-blue-600 text-white px-6 py-2 rounded-md inline-block'
        >
          Get in Touch
        </Link>
      </section>
    </div>
  );
}
