'use client';

import { FileText } from 'lucide-react';
import React, { useState } from 'react';

import { Button } from '@/components/ui/button';
import { toast } from '@/hooks/use-toast';
import Deck from '@/deck/types/deck';
import LoadingSpinner from '@/components/common/LoadingSpinner';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from '@/components/ui/tooltip';

const DumpButton = ({ deck }: { deck: Deck }) => {
  const [isDumping, setIsDumping] = useState(false);

  const handleDump = async () => {
    try {
      setIsDumping(true);

      const response = await fetch(`/api/v2/deck/${deck.id}/dump`, {
        method: 'GET',
        headers: {
          Accept: 'application/json',
        },
        credentials: 'include',
      });

      if (!response.ok) {
        const text = await response.text();
        console.error('Failed to dump deck:', response.status, text);
        toast({ title: 'Error', description: 'Failed to dump deck', variant: 'destructive' });
        setIsDumping(false);
        return;
      }

      const json = await response.json();
      const blob = new Blob([JSON.stringify(json, null, 2)], { type: 'application/json' });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;

      const filename = `${(deck.name || 'deck').toLowerCase().replace(/\s+/g, '_')}_dump.json`;

      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);

      toast({
        title: 'Dump Successful',
        description: `Deck "${deck.name}" has been dumped successfully.`,
      });
    } catch (err) {
      console.error(err);
      toast({
        title: 'Error',
        description: 'Failed to dump deck',
        variant: 'destructive',
      });
    } finally {
      setIsDumping(false);
    }
  };

  return (
    <TooltipProvider>
      <Tooltip>
        <TooltipTrigger asChild>
          <Button
            onClick={handleDump}
            disabled={isDumping || !deck?.id}
            className='flex items-center px-3 py-2 hover:bg-gray-50 bg-gray-100 text-gray-800'
            variant='outline'
          >
            {isDumping ? (
              <LoadingSpinner size={12} />
            ) : (
              <>
                <FileText size={16} /> Dump
              </>
            )}
          </Button>
        </TooltipTrigger>
        <TooltipContent>
          <p>Dump your deck data to a JSON file for backup or transfer.</p>
        </TooltipContent>
      </Tooltip>
    </TooltipProvider>
  );
};

export default DumpButton;
