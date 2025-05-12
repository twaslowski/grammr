import { BookPlus } from 'lucide-react';
import React, { useState } from 'react';

import FlashcardPreview from '@/components/flashcard/FlashcardPreview';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from '@/components/ui/dialog';
import { Toaster } from '@/components/ui/toaster';
import { SignedIn, SignedOut } from '@clerk/nextjs';
import { toast } from '@/hooks/use-toast';

interface GenericFlashcardExportProps {
  front: string;
  back: string;
  layout: string;
}

const GenericFlashcardExport: React.FC<GenericFlashcardExportProps> = ({
  front,
  back,
  layout,
}) => {
  const [showPreviewDialog, setShowPreviewDialog] = useState(false);

  const displayAuthToast = () => {
    toast({
      title: 'Not logged in',
      description: 'You need to log in to create flashcards',
      variant: 'destructive',
    });
  };

  return (
    <div>
      <SignedIn>
        <div className='space-y-4'>
          <Button className={layout} onClick={() => setShowPreviewDialog(true)}>
            <BookPlus className='h-4 w-4 mr-2' />
            To Vocabulary
          </Button>
          <Toaster />
          <Dialog open={showPreviewDialog} onOpenChange={setShowPreviewDialog}>
            <DialogContent className='max-w-3xl'>
              <DialogHeader>
                <DialogTitle>Preview Flashcard</DialogTitle>
              </DialogHeader>
              <FlashcardPreview
                initialFront={front}
                initialBack={back}
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

export default GenericFlashcardExport;
