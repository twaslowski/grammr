import { BookPlus } from 'lucide-react';
import React, { useState } from 'react';

import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Toaster } from '@/components/ui/toaster';
import { SignedIn, SignedOut } from '@clerk/nextjs';
import TokenType from '@/types/tokenType';
import TokenFlashcardPreview from '@/flashcard/component/TokenFlashcardPreview';

interface TokenFlashcardExportProps {
  token: TokenType;
}

const TokenFlashcardExport: React.FC<TokenFlashcardExportProps> = ({
  token,
}) => {
  const [showPreviewDialog, setShowPreviewDialog] = useState(false);

  return (
    <div>
      <SignedIn>
        <div className='space-y-4'>
          <Button onClick={() => setShowPreviewDialog(true)}>
            <BookPlus className='h-4 w-4 mr-2' />
            To Vocabulary
          </Button>
          <Toaster />
          <Dialog open={showPreviewDialog} onOpenChange={setShowPreviewDialog}>
            <DialogContent className='max-w-3xl'>
              <DialogHeader>
                <DialogTitle>Preview Flashcard</DialogTitle>
              </DialogHeader>
              <TokenFlashcardPreview
                token={token}
                onClose={() => setShowPreviewDialog(false)}
              />
            </DialogContent>
          </Dialog>
        </div>
      </SignedIn>
      <SignedOut>
        <Toaster />
        <Button className='cursor-not-allowed bg-gray-500' disabled>
          <BookPlus className='h-4 w-4 mr-2' />
          <span className='hidden sm:inline'>
            To Vocabulary (Sign in required)
          </span>
        </Button>
      </SignedOut>
    </div>
  );
};

export default TokenFlashcardExport;
