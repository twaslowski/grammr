'use client';

import React from 'react';
import { ChatMessage } from '@/chat/types/message';
import { UserMessageBubble } from '@/chat/components/message/UserMessageBubble';
import { AssistantMessageBubble } from '@/chat/components/message/AssistantMessageBubble';
import { AssistantFailedMessageBubble } from '@/chat/components/message/FailedMessageBubble';
import { LoadingMessageBubble } from '@/chat/components/message/LoadingMessageBubble';

export const MessageBubble: React.FC<{ message: ChatMessage }> = ({ message }) => {
  if (message.role === 'user') {
    return <UserMessageBubble message={message} />;
  }
  if (message.role === 'assistant') {
    switch (message.status) {
      case 'failed':
        return <AssistantFailedMessageBubble message={message} />;
      case 'streaming':
        return <LoadingMessageBubble message={message} />;
      default:
        return <AssistantMessageBubble message={message} />;
    }
  }
  return null;
};
