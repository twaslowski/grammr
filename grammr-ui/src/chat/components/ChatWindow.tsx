import React, { useState } from 'react';
import { MessageBubble } from '@/chat/components/message/MessageBubble';
import { useChat } from '@/chat/hooks/useChat';

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
    <div className='flex flex-col h-full max-w-2xl mx-auto'>
      {/* Chat messages area */}
      <div className='flex-1 overflow-y-auto p-4 space-y-2'>
        {messages.map((msg) => (
          <MessageBubble key={msg.id} message={msg} />
        ))}
      </div>

      {/* Input box */}
      <div className='border-t p-4'>
        <textarea
          className='w-full border rounded-lg p-2 resize-none'
          rows={2}
          placeholder='Type your message...'
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyPress={handleKeyPress}
        />
        <button
          onClick={handleSend}
          className='mt-2 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600'
        >
          Send
        </button>
      </div>
    </div>
  );
};
