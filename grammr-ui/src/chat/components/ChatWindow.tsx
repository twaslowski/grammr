import React, { useEffect } from 'react';
import { MessageBubble } from '@/chat/components/message/MessageBubble';
import { Suggestions } from '@/chat/components/Suggestions';
import { InputArea } from '@/components/common/InputArea';
import useMessages from '@/chat/hooks/use-messages';
import { useChat } from '@/context/ChatContext';
import { LoadingMessageBubble } from '@/chat/components/message/LoadingMessageBubble';
import { AssistantMessageBubble } from '@/chat/components/message/AssistantMessageBubble';
import { AssistantFailedMessageBubble } from '@/chat/components/message/FailedMessageBubble';

export const ChatWindow: React.FC = () => {
  const { chatId, setChatId, refreshChats } = useChat();
  const { messages, sendMessage, startChat, refreshMessages, isLoading, error } =
    useMessages(chatId);

  const handleSend = async (text: string) => {
    if (!chatId) {
      const newChatId = await startChat(text);
      setChatId(newChatId);
    } else {
      await sendMessage(text);
    }
  };

  useEffect(() => {
    if (chatId) {
      void refreshMessages();
      void refreshChats();
    }
  }, [chatId, refreshMessages, refreshChats]);

  return (
    <div className='flex h-full bg-white max-w-3xl mx-auto'>
      <div className='flex-1 overflow-y-auto p-4'>
        {messages.map((msg) => (
          <MessageBubble key={msg.id} message={msg} />
        ))}
        {isLoading && <LoadingMessageBubble />}
        {error && <AssistantFailedMessageBubble />}
      </div>

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
