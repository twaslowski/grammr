'use client';

import { useEffect, useRef, useState } from 'react';
import { SendHorizonal } from 'lucide-react';
import { cn } from '@/lib/utils';
import { useChat } from '@/chat/hooks/useChat';
import { TypingDots } from '@/chat/components/TypingDots';

export default function Chat() {
  const { messages, sendMessage, isLoading, error } = useChat();
  const [input, setInput] = useState('');
  const messagesEndRef = useRef<HTMLDivElement | null>(null);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  return (
    <div className='flex flex-col h-[80vh] w-full max-w-2xl mx-auto border rounded-2xl shadow-md overflow-hidden'>
      <div className='flex-1 overflow-y-auto p-4 space-y-2 bg-white'>
        {messages.map((msg, index) => (
          <div
            key={index}
            className={cn(
              'max-w-[70%] px-4 py-2 rounded-xl text-sm whitespace-pre-line',
              msg.role === 'user' ? 'ml-auto bg-blue-100' : 'mr-auto bg-gray-100',
            )}
          >
            {msg.content === '...' ? <TypingDots /> : msg.content}
          </div>
        ))}
        <div ref={messagesEndRef} />
      </div>

      <form
        onSubmit={(e) => {
          e.preventDefault();
          void sendMessage(input);
          setInput('');
        }}
        className='border-t p-3 bg-white flex items-center gap-2'
      >
        <input
          type='text'
          value={input}
          onChange={(e) => setInput(e.target.value)}
          placeholder='Type your message...'
          className='flex-1 px-4 py-2 border rounded-full bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500'
        />
        <button
          type='submit'
          className='p-2 text-blue-600 hover:text-blue-800'
          disabled={!input.trim()}
        >
          <SendHorizonal className='w-5 h-5' />
        </button>
      </form>
    </div>
  );
}
