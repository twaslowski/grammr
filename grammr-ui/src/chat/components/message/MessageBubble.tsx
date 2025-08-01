'use client';

import React from 'react';
import { Message } from '@/chat/types/message';
import { UserMessageBubble } from '@/chat/components/message/UserMessageBubble';
import { AssistantMessageBubble } from '@/chat/components/message/AssistantMessageBubble';

export const MessageBubble: React.FC<{ message: Message }> = ({ message }) => {
  if (message.role === 'USER') {
    return <UserMessageBubble message={message} />;
  }
  if (message.role === 'ASSISTANT') {
    return <AssistantMessageBubble message={message} />;
  }
  return null;
};
