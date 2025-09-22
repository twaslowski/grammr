import React from 'react';
import { SignedIn, SignedOut } from '@clerk/nextjs';
import { useChat } from '@/context/ChatContext';
import { PlusIcon } from '@/components/ui/icons';
import { useRouter } from 'next/navigation';

export const ChatHistory: React.FC = () => {
  const { setChatId, chats } = useChat();
  const router = useRouter();

  return (
    <div>
      <h2 className='text-lg font-semibold mb-2'>Chat History</h2>
      <div className='p-2 rounded-full hover:bg-gray-200 flex items-center gap-2 '>
        <PlusIcon size={12} />
        <p
          className='text-sm font-light cursor-pointer'
          onClick={() => {
            router.push('/');
            setChatId('');
          }}
        >
          New Chat
        </p>
      </div>

      <SignedIn>
        <div className='width-full'>
          {chats.length === 0 && <p className='text-gray-500'>Your chats will appear here.</p>}
          {chats
            .sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime())
            .map((chat) => (
              <div key={chat.chatId} className='p-2 rounded-full hover:bg-gray-200'>
                <p
                  className='text-sm cursor-pointer'
                  onClick={() => {
                    router.push('/');
                    setChatId(chat.chatId);
                  }}
                >
                  {chat.summary || 'Untitled Chat'}
                </p>
              </div>
            ))}
        </div>
      </SignedIn>
      <SignedOut>
        <div>
          <p className='text-gray-500'>Please sign in to view your chat history.</p>
        </div>
      </SignedOut>
    </div>
  );
};
