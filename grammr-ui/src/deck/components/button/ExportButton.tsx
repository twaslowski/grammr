'use client';

import { Download } from 'lucide-react';
import React, { useState } from 'react';

import { Button } from '@/components/ui/button';
import { toast } from '@/hooks/use-toast';
import Deck from '@/deck/types/deck';
import LoadingSpinner from '@/components/common/LoadingSpinner';

const ExportButton = ({ deck }: { deck: Deck }) => {
  const [isExporting, setIsExporting] = useState(false);

  const handleExport = async () => {
    try {
      setIsExporting(true);

      const response = await fetch('/api/v1/deck/export', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/octet-stream',
        },
        body: JSON.stringify({
          deckId: deck.id,
          exportDataType: 'APKG'
        })
      });

      if (!response.ok) {
        throw new Error(`Failed to export deck: ${response.statusText}`);
      }

      const blob = await response.blob();

      const url = window.URL.createObjectURL(new Blob([blob]));
      const link = document.createElement('a');
      link.href = url;

      let filename = `${deck.name.toLowerCase().replace(' ', '_') || 'deck'}.apkg`;

      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);

      toast({
        title: 'Export Successful',
        description: `Deck "${deck.name}" has been exported successfully.`,
      });
    } catch {
      toast({
        title: 'Error',
        description: 'Export failed',
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
      className='flex items-center px-3 py-2 hover:bg-gray-50 bg-gray-100 text-gray-800'
      variant='outline'
    >
      {isExporting ? (
        <LoadingSpinner size={12}></LoadingSpinner>
      ) : (
        <>
          <Download size={16} />
          APKG
        </>
      )}
    </Button>
  );
};

export default ExportButton;
