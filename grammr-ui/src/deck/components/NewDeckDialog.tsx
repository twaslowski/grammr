import React, { useRef, useState } from 'react';

import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import { toast } from '@/hooks/use-toast';

interface NewDeckDialogProps {
  isOpen: boolean;
  onClose: () => void;
  onCreate: (name: string, description: string) => void;
  onImport?: () => void;
}

const NewDeckDialog: React.FC<NewDeckDialogProps> = ({ isOpen, onClose, onCreate, onImport }) => {
  const [newDeckName, setNewDeckName] = useState('');
  const [newDeckDescription, setNewDeckDescription] = useState('');
  const [importFile, setImportFile] = useState<File | null>(null);
  const [isImporting, setIsImporting] = useState(false);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const handleCreate = () => {
    onCreate(newDeckName, newDeckDescription);
    setNewDeckName('');
    setNewDeckDescription('');
    onClose();
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files && e.target.files[0] ? e.target.files[0] : null;
    setImportFile(file);
  };

  const handleImport = async () => {
    if (!importFile) {
      toast({
        title: 'No file selected',
        description: 'Please choose a JSON dump file to import.',
        variant: 'destructive',
      });
      return;
    }

    try {
      setIsImporting(true);
      const text = await importFile.text();
      const payload = JSON.parse(text);

      const resp = await fetch('/api/v2/deck/import', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(payload),
      });

      if (!resp.ok) {
        const body = await resp.text();
        console.error('Import failed', resp.status, body);
        toast({
          title: 'Import failed',
          description: 'Server rejected the import file.',
          variant: 'destructive',
        });
        return;
      }

      // Call onImport (if provided) before closing to ensure parent can refresh state
      if (onImport) {
        try {
          await onImport();
        } catch (err) {
          // Even if refresh fails, proceed to close and notify success of import
          console.error('refresh after import failed', err);
        }
      }

      toast({ title: 'Import successful', description: 'Deck imported into your account.' });
      setImportFile(null);
      onClose();
    } catch (err) {
      console.error(err);
      toast({
        title: 'Import failed',
        description: 'Could not parse or upload file.',
        variant: 'destructive',
      });
    } finally {
      setIsImporting(false);
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={onClose}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Create New Deck</DialogTitle>
        </DialogHeader>
        <div className='space-y-4'>
          <Input
            placeholder='Deck name'
            value={newDeckName}
            onChange={(e) => setNewDeckName(e.target.value)}
          />
          <Input
            placeholder='Description'
            value={newDeckDescription}
            onChange={(e) => setNewDeckDescription(e.target.value)}
          />
          <div className='pt-4 border-t border-gray-100'></div>
          <div className='text-sm text-gray-600'>Or import a deck dump (JSON)</div>
          <div className='flex items-center gap-2 mt-2'>
            {/* hidden file input */}
            <Input
              ref={fileInputRef}
              type='file'
              accept='application/json'
              onChange={handleFileChange}
              className='hidden'
              data-testid='deck-import-input'
            />

            {/* visible filename display (read-only) */}
            <Input
              value={importFile ? importFile.name : ''}
              placeholder='No file selected'
              readOnly
              className='w-64'
            />

            <Button variant='outline' onClick={() => fileInputRef.current?.click()}>
              Choose file
            </Button>
          </div>
          <div className='flex justify-end gap-2'>
            <Button variant='outline' onClick={onClose}>
              Cancel
            </Button>
            <Button onClick={handleCreate} disabled={!newDeckName}>
              Create
            </Button>
            <Button onClick={handleImport} disabled={!importFile || isImporting}>
              {isImporting ? 'Importing...' : 'Import'}
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default NewDeckDialog;
