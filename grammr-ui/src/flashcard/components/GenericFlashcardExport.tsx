import { BookPlus } from 'lucide-react';
import React, { useState } from 'react';

import GenericFlashcardPreview from '@/flashcard/components/GenericFlashcardPreview';
import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { SignedIn, SignedOut } from '@clerk/nextjs';
import { Paradigm } from '@/flashcard/types/paradigm';

interface GenericFlashcardExportProps {
  front: string;
  back: string;
  paradigm: Paradigm | null;
  layout?: string;
}

const GenericFlashcardExport: React.FC<GenericFlashcardExportProps> = ({
  front,
  back,
  layout,
  paradigm,
}) => {
  const [showPreviewDialog, setShowPreviewDialog] = useState(false);

  return (
    <div>
      <SignedIn>
        <div className='space-y-4'>
          <Button className={layout} onClick={() => setShowPreviewDialog(true)}>
            <BookPlus className='h-4 w-4 mr-2' />
            To Vocabulary
          </Button>
          <Dialog open={showPreviewDialog} onOpenChange={setShowPreviewDialog}>
            <DialogContent className='max-w-3xl'>
              <DialogHeader>
                <DialogTitle>Preview Flashcard</DialogTitle>
              </DialogHeader>
              <GenericFlashcardPreview
                initialFront={front}
                initialBack={back}
                paradigm={paradigm === null ? undefined : paradigm}
                onClose={() => setShowPreviewDialog(false)}
              />
            </DialogContent>
          </Dialog>
        </div>
      </SignedIn>
      <SignedOut>
        <Button className='cursor-not-allowed bg-gray-500' disabled>
          <BookPlus className='h-4 w-4 mr-2' />
          <span className='hidden sm:inline'>To Vocabulary (Sign in required)</span>
        </Button>
      </SignedOut>
    </div>
  );
};

export default GenericFlashcardExport;
