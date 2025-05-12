import { Github, Linkedin, Mail, Send } from 'lucide-react';
import React from 'react';

export default function Contact() {
  return (
    <main>
      <div className='container mx-auto px-4 py-12 flex-grow'>
        <section className='mb-8 text-center'>
          <h1 className='text-4xl font-bold mb-4'>Get in Touch</h1>
          <p className='text-gray-600'>
            Feel free to reach out through any of these platforms.
          </p>
        </section>

        <div className='bg-gray-50 rounded-lg p-6 mb-8'>
          <ul className='space-y-4'>
            <li>
              <a
                href='mailto:tobiaswaslowski@gmail.com'
                className='flex items-center p-3 hover:bg-gray-100 rounded-md transition-colors'
              >
                <Mail className='text-blue-500 mr-4' size={24} />
                <div>
                  <h2 className='font-medium'>Email</h2>
                  <p className='text-gray-600'>tobiaswaslowski@gmail.com</p>
                </div>
              </a>
            </li>

            <li>
              <a
                href='https://t.me/violin_tobi'
                target='_blank'
                rel='noopener noreferrer'
                className='flex items-center p-3 hover:bg-gray-100 rounded-md transition-colors'
              >
                <Send className='text-blue-500 mr-4' size={24} />
                <div>
                  <h2 className='font-medium'>Telegram</h2>
                  <p className='text-gray-600'>@violin_tobi</p>
                </div>
              </a>
            </li>

            <li>
              <a
                href='https://www.linkedin.com/in/twaslowski'
                target='_blank'
                rel='noopener noreferrer'
                className='flex items-center p-3 hover:bg-gray-100 rounded-md transition-colors'
              >
                <Linkedin className='text-blue-500 mr-4' size={24} />
                <div>
                  <h2 className='font-medium'>LinkedIn</h2>
                  <p className='text-gray-600'>linkedin.com/in/twaslowski</p>
                </div>
              </a>
            </li>

            <li>
              <a
                href='https://github.com/twaslowski'
                target='_blank'
                rel='noopener noreferrer'
                className='flex items-center p-3 hover:bg-gray-100 rounded-md transition-colors'
              >
                <Github className='text-blue-500 mr-4' size={24} />
                <div>
                  <h2 className='font-medium'>GitHub</h2>
                  <p className='text-gray-600'>github.com/twaslowski</p>
                </div>
              </a>
            </li>
          </ul>
        </div>
      </div>
    </main>
  );
}
