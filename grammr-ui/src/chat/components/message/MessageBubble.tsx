'use client';

import React from 'react';
import { Message } from '@/chat/types/message';
import { UserMessageBubble } from '@/chat/components/message/UserMessageBubble';
import { AssistantMessageBubble } from '@/chat/components/message/AssistantMessageBubble';
import { LoadingMessageBubble } from '@/chat/components/message/LoadingMessageBubble';

export const MessageBubble: React.FC<{ message: Message; isLoading: boolean }> = ({
  message,
  isLoading,
}) => {
  if (message.role === 'USER') {
    return <UserMessageBubble message={message} />;
  }
  if (message.role === 'ASSISTANT') {
    if (isLoading) {
      return <LoadingMessageBubble message={message} />;
    } else {
      return <AssistantMessageBubble message={message} />;
    }
  }
  return null;
};
