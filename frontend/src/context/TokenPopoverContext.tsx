'use client';

import { createContext, ReactNode, useContext, useState } from 'react';
import TokenType from '@/token/types/tokenType';
import TokenPopover from '@/token/components/TokenPopover';

export interface TokenPopoverContextType {
  show: (token: TokenType, context: string, languageCode: string) => void;
  hide: () => void;
}

const TokenPopoverContext = createContext<TokenPopoverContextType | undefined>(undefined);

export const TokenPopoverProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<TokenType | null>(null);
  const [contextText, setContextText] = useState('');
  const [languageCode, setLanguageCode] = useState('');

  const show = (token: TokenType, context: string, languageCode: string) => {
    setToken(token);
    setContextText(context);
    setLanguageCode(languageCode);
  };

  const hide = () => setToken(null);

  return (
    <TokenPopoverContext.Provider value={{ show, hide }}>
      {children}
      {token && (
        <TokenPopover
          token={token}
          context={contextText}
          onClose={hide}
          languageCode={languageCode}
        />
      )}
    </TokenPopoverContext.Provider>
  );
};

export const useTokenPopover = (): TokenPopoverContextType => {
  const context = useContext(TokenPopoverContext);
  if (!context) {
    throw new Error('useTokenPopover must be used within a TokenPopoverProvider');
  }
  return context;
};
