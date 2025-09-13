import Deck from '@/deck/types/deck';
import { useApi } from '@/hooks/useApi';
import { Button } from '@/components/ui/button';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';
import { RotateCcw } from 'lucide-react';
import React from 'react';
import { toast } from '@/hooks/use-toast';

export default function ResetSyncButton({ deck }: { deck: Deck }) {
  const { isLoading, request, error } = useApi();

  const resetSync = async (deckId: string): Promise<void> => {
    await request<void>(`/api/v2/deck/${deckId}/reset-sync`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
    });
    if (error) {
      toast({
        title: 'Failed to reset sync status',
        description: error.message,
        variant: 'destructive',
      });
    }
  };

  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <Button
            onClick={() => resetSync(deck.id)}
            disabled={isLoading || !deck?.id}
            className='flex items-center px-3 py-2 rounded hover:bg-orange-50 bg-orange-100 text-orange-800'
            variant='outline'
          >
            <span className={isLoading ? 'animate-spin' : ''}>
              <RotateCcw className='text-orange-800' size={16} />
            </span>
            {isLoading ? 'Resetting ...' : 'Reset'}
          </Button>
        </TooltipTrigger>
        <TooltipContent>
          <p>Resets the sync status of all your flashcards to entirely recreate the deck in Anki</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
}
