'use client';

import { ArrowLeft, Trash2, ChevronLeft, ChevronRight } from 'lucide-react';
import { useRouter } from 'next/navigation';
import React, { use, useCallback, useEffect, useState } from 'react';

import LoadingSpinner from '@/components/common/LoadingSpinner';
import NotFound from '@/components/common/NotFound';
import EditableText from '@/components/common/EditableText';
import { FlashcardList } from '@/flashcard/components/FlashcardList';
import Deck from '@/deck/types/deck';
import SyncButton from '@/deck/components/button/SyncButton';
import { Button } from '@/components/ui/button';
import { toast } from '@/hooks/use-toast';
import { useApi } from '@/hooks/useApi';
import { Flashcard } from '@/flashcard/types/flashcard';
import { PagedFlashcardResponse } from '@/flashcard/types/pagedFlashcardResponse';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import GenericFlashcardPreview from '@/flashcard/components/GenericFlashcardPreview';
import ResetSyncButton from '@/deck/components/button/ResetSyncButton';
import DumpButton from '@/deck/components/button/DumpButton';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import Error from '@/components/common/Error';

export default function DeckPage(props: { params: Promise<{ deckId: string }> }) {
  const { deckId } = use(props.params);
  const { isLoading, error, request } = useApi();
  const [deck, setDeck] = useState<Deck | null>(null);
  const [flashcards, setFlashcards] = useState<Flashcard[]>([]);
  const [showPreviewDialog, setShowPreviewDialog] = useState(false);
  const [totalCards, setTotalCards] = useState(0);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [totalPages, setTotalPages] = useState(0);
  const router = useRouter();

  const handleDeleteDeck = async () => {
    if (!confirm('Are you sure you want to delete this deck? This action cannot be undone.')) {
      return;
    }

    await fetch(`/api/v2/deck/${deckId}`, {
      method: 'DELETE',
    })
      .then(() => {
        router.push('/user/decks');
      })
      .catch(() => {
        toast({
          title: 'Error',
          description: 'Failed to delete deck. Please try again later.',
          variant: 'destructive',
        });
      });
  };

  const fetchFlashcards = useCallback(
    async (page: number = currentPage, size: number = pageSize) => {
      const params = new URLSearchParams({
        paginated: 'true',
        page: page.toString(),
        size: size.toString(),
        sortBy: 'front',
        sortDirection: 'asc',
      });

      const response = await request<PagedFlashcardResponse>(
        `/api/v2/deck/${deckId}/flashcard?${params}`,
        {
          method: 'GET',
        },
      );

      setFlashcards(response.content);
      setTotalCards(response.totalElements);
      setTotalPages(response.totalPages);
      setCurrentPage(response.page);
    },
    [deckId, request, currentPage, pageSize],
  );

  const handlePageChange = (newPage: number) => {
    setCurrentPage(newPage);
    void fetchFlashcards(newPage, pageSize);
  };

  const handlePageSizeChange = (newSize: string) => {
    const size = parseInt(newSize);
    setPageSize(size);
    setCurrentPage(0);
    void fetchFlashcards(0, size);
  };
  const handleUpdateDeck = async (field: 'name' | 'description', value: string) => {
    try {
      const updatedDeck = await request<Deck>(`/api/v2/deck/${deckId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ [field]: value }),
      });

      setDeck(updatedDeck);

      toast({
        title: 'Success',
        description: `Deck ${field} updated successfully.`,
      });
    } catch (error) {
      toast({
        title: 'Error',
        description: `Failed to update deck ${field}. Please try again.`,
        variant: 'destructive',
      });
      throw error;
    }
  };

  useEffect(() => {
    const fetchDeck = async () => {
      const fetchedDeck = await request<Deck>(`/api/v2/deck/${deckId}`, {
        method: 'GET',
      });
      setDeck(fetchedDeck);
    };

    void fetchDeck();
    void fetchFlashcards();
  }, [deckId, request, fetchFlashcards]);

  if (isLoading) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <LoadingSpinner message='Loading deck...' />;
      </div>
    );
  }

  if (error && error.code === 404) {
    return <NotFound message='Deck not found' backHref='/user/decks' />;
  }

  if (error) {
    return (
      <div className='flex flex-col items-center justify-center min-h-screen p-4'>
        <Error title='Failed to load deck'>
          An unexpected error occurred when loading this deck. Please try again later.
        </Error>
      </div>
    );
  }

  if (deck)
    return (
      <div className='container mx-auto px-4 py-8'>
        <button
          onClick={() => router.push('/user/decks')}
          className='flex items-center text-primary-600 hover:text-primary-800 mb-6'
        >
          <ArrowLeft size={16} className='mr-1' />
          Back to Decks
        </button>

        <div className='bg-white rounded-lg shadow-md p-6 mb-8'>
          <div className='flex flex-col md:flex-row md:justify-between md:items-center mb-4'>
            <div>
              <EditableText
                value={deck.name}
                onSaveAction={(name) => handleUpdateDeck('name', name)}
                placeholder='Deck Name'
                className='text-2xl font-bold mb-2'
              />
              <EditableText
                value={deck.description}
                onSaveAction={(description) => handleUpdateDeck('description', description)}
                placeholder='Deck Description'
                className='text-gray-600'
                multiline={true}
              />
            </div>

            <div className='flex space-x-2 mt-4 md:mt-0'>
              <DumpButton deck={deck} />
              <SyncButton deck={deck} onSync={() => fetchFlashcards(currentPage, pageSize)} />
              <ResetSyncButton
                deck={deck}
                onSuccess={() => fetchFlashcards(currentPage, pageSize)}
              />

              <Button
                onClick={handleDeleteDeck}
                className='flex items-center px-3 py-2 rounded hover:bg-red-50 bg-red-100 text-red-800'
                variant='outline'
              >
                <Trash2 size={16} className='mr-1' />
                <span className='hidden sm:inline'>Delete</span>
              </Button>
            </div>
          </div>

          <div className='grid grid-cols-1 md:grid-cols-3 gap-4 text-sm'>
            <div className='bg-gray-50 p-3 rounded'>
              <div className='text-gray-500 mb-1'>Total Cards</div>
              <div className='font-semibold'>{totalCards || 0}</div>
            </div>

            <div className='bg-gray-50 p-3 rounded'>
              <div className='text-gray-500 mb-1'>Created On</div>
              <div className='font-semibold'>
                {new Date(deck.createdTimestamp).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </div>
            </div>

            <div className='bg-gray-50 p-3 rounded'>
              <div className='text-gray-500 mb-1'>Last Modified</div>
              <div className='font-semibold'>
                {new Date(deck.updatedTimestamp).toLocaleDateString('en-US', {
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                })}
              </div>
            </div>
          </div>
        </div>

        <div className='bg-white rounded-lg shadow-md p-6'>
          <div className='flex justify-between items-center mb-6'>
            <h2 className='text-xl font-semibold'>Flashcards</h2>
            <div className='flex items-center space-x-2'>
              <Button onClick={() => setShowPreviewDialog(true)}>Add card</Button>
            </div>
            <Dialog open={showPreviewDialog} onOpenChange={setShowPreviewDialog}>
              <DialogContent className='max-w-3xl'>
                <DialogHeader>
                  <DialogTitle>Preview Flashcard</DialogTitle>
                </DialogHeader>
                <GenericFlashcardPreview
                  initialDeckId={deck.id}
                  initialFront={''}
                  initialBack={''}
                  paradigm={null}
                  onClose={() => setShowPreviewDialog(false)}
                  onCardAdded={() => fetchFlashcards(currentPage, pageSize)}
                />
              </DialogContent>
            </Dialog>
          </div>

          {totalCards > 0 && (
            <div className='flex items-center justify-between mb-4 p-4 bg-gray-50 rounded-lg'>
              <div className='flex items-center space-x-4'>
                <span className='text-sm text-gray-600'>
                  Showing {currentPage * pageSize + 1} to{' '}
                  {Math.min((currentPage + 1) * pageSize, totalCards)} of {totalCards} cards
                </span>
                <Select value={pageSize.toString()} onValueChange={handlePageSizeChange}>
                  <SelectTrigger className='w-20'>
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value='10'>10</SelectItem>
                    <SelectItem value='20'>20</SelectItem>
                    <SelectItem value='50'>50</SelectItem>
                    <SelectItem value='100'>100</SelectItem>
                  </SelectContent>
                </Select>
                <span className='text-sm text-gray-600'>per page</span>
              </div>

              <div className='flex items-center space-x-2'>
                <Button
                  onClick={() => handlePageChange(currentPage - 1)}
                  disabled={currentPage === 0}
                  variant='outline'
                  size='sm'
                >
                  <ChevronLeft size={16} />
                  Previous
                </Button>

                <span className='text-sm text-gray-600'>
                  Page {currentPage + 1} of {totalPages}
                </span>

                <Button
                  onClick={() => handlePageChange(currentPage + 1)}
                  disabled={currentPage >= totalPages - 1}
                  variant='outline'
                  size='sm'
                >
                  Next
                  <ChevronRight size={16} />
                </Button>
              </div>
            </div>
          )}

          {flashcards.length > 0 ? (
            <FlashcardList cards={flashcards} deckId={deck.id} />
          ) : (
            <div className='text-center py-12 bg-gray-50 rounded-lg'>
              <p className='text-gray-500 mb-4'>This deck doesn't have any flashcards yet.</p>
            </div>
          )}
        </div>
      </div>
    );
}
