import React from 'react';
import { MessageBubble } from '@/chat/components/message/MessageBubble';
import { useChat } from '@/chat/hooks/useChat';
import { Suggestions } from '@/chat/components/Suggestions';
import { InputArea } from '@/components/common/InputArea';

export const ChatWindow: React.FC = () => {
  const { messages, sendMessage } = useChat();

  const handleSend = async (text: string) => {
    await sendMessage({
      id: '', // ID will be generated in the hook
      role: 'user',
      content: text,
      analysis: null,
      timestamp: Date.now(),
    });
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
              onSuggestionClickAction={(text: string) => {
                void handleSend(text);
              }}
            />
          )}
        </div>
        <InputArea onEnter={handleSend} clear={true} />
      </div>
    </div>
  );
};
