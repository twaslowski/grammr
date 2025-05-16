'use client';

import { Download } from 'lucide-react';
import React, { useState } from 'react';

import { Button } from '@/components/ui/button';
import { toast } from '@/hooks/use-toast';
import Deck from '@/flashcard/types/deck';

const ExportButton = ({ deck }: { deck: Deck }) => {
  const [isExporting, setIsExporting] = useState(false);

  const handleExport = async () => {
    if (!deck?.id) {
      toast({
        title: 'Export Failed',
        description: 'Invalid deck information',
        variant: 'destructive',
      });
      return;
    }

    try {
      setIsExporting(true);

      const response = await fetch('/api/v1/anki/export', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/octet-stream',
        },
        body: JSON.stringify({
          deckId: deck.id,
          exportDataType: 'APKG',
          credentials: 'include',
        }),
        credentials: 'include',
      });

      if (!response.ok) {
        throw new Error(`Failed to export deck: ${response.statusText}`);
      }
      const blob = await response.blob();

      const url = window.URL.createObjectURL(new Blob([blob]));
      const link = document.createElement('a');
      link.href = url;

      const contentDisposition = response.headers.get('content-disposition');
      let filename = `${deck.name || 'deck'}-export.apkg`;

      if (contentDisposition) {
        const filenameMatch = contentDisposition.match(/filename="?([^"]*)"?/);
        if (filenameMatch && filenameMatch[1]) {
          filename = filenameMatch[1];
        }
      }

      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);

      toast({
        title: 'Export Successful',
        description: `Deck "${deck.name}" has been exported successfully.`,
      });
    } catch (error) {
      let message;
      if (error instanceof Error) message = error.message;
      else message = 'An unknown error occurred';
      console.error('Export failed:', error);
      toast({
        title: 'Export Failed',
        description: message,
        variant: 'destructive',
      });
    } finally {
      setIsExporting(false);
    }
  };

  return (
    <Button
      onClick={handleExport}
      disabled={isExporting || !deck?.id}
      className='flex items-center px-3 py-2 rounded bg-green-100 text-green-800'
      variant='outline'
    >
      {isExporting ? (
        <>
          <span className='animate-spin mr-2'>
            <svg
              className='w-4 h-4'
              xmlns='http://www.w3.org/2000/svg'
              fill='none'
              viewBox='0 0 24 24'
            >
              <circle
                className='opacity-25'
                cx='12'
                cy='12'
                r='10'
                stroke='currentColor'
                strokeWidth='4'
              ></circle>
              <path
                className='opacity-75'
                fill='currentColor'
                d='M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z'
              ></path>
            </svg>
          </span>
          Exporting...
        </>
      ) : (
        <>
          <Download size={16} className='mr-2' />
          Export to Anki
        </>
      )}
    </Button>
  );
};

export default ExportButton;
