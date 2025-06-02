import React, { useState } from 'react';
import { MessageBubble } from '@/chat/components/message/MessageBubble';
import { useChat } from '@/chat/hooks/useChat';
import { SendHorizonal } from 'lucide-react';
import { Suggestions } from '@/chat/components/Suggestions';

export const ChatWindow: React.FC = () => {
  const { messages, sendMessage } = useChat();
  const [input, setInput] = useState('');

  const handleSend = async () => {
    if (!input.trim()) return;

    setInput('');

    await sendMessage({
      id: '', // ID will be generated in the hook
      role: 'user',
      content: input,
      analysis: null,
      timestamp: Date.now(),
    });
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      void handleSend();
    }
  };

  return (
    <div className='flex h-full bg-white max-w-3xl mx-auto'>
      {/* Chat messages */}
      <div className='flex-1 overflow-y-auto p-4'>
        {messages.map((msg) => (
          <MessageBubble key={msg.id} message={msg} />
        ))}
      </div>

      {/* Input area */}
      <div className='fixed bottom-0 bg-white border-t p-4 w-full max-w-3xl mx-auto'>
        <div>
          {messages.length === 0 && (
            <Suggestions
              onSuggestionClick={(text: string) => {
                setInput(text);
                void handleSend();
              }}
            />
          )}
        </div>
        <div className='w-full border border-gray-300 rounded-2xl px-4 resize-none shadow-sm flex flex-col py-2'>
          <form
            onSubmit={(e) => {
              e.preventDefault();
              void handleSend();
            }}
          >
            <textarea
              className='border-none w-full resize-none focus:outline-none focus:ring-0'
              placeholder='Type your message...'
              rows={1}
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={handleKeyPress}
            />
          </form>
          <button
            type='submit'
            aria-label='Send message'
            className='self-end p-2 rounded-full bg-black text-white hover:bg-gray-800 transition-colors shadow-md'
            onClick={() => void handleSend()}
          >
            <SendHorizonal className='w-5 h-5' />
          </button>
        </div>
      </div>
    </div>
  );
};
