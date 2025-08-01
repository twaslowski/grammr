import React, { useState } from 'react';

import { Button } from '@/components/ui/button';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';

interface NewDeckDialogProps {
  isOpen: boolean;
  onClose: () => void;
  onCreate: (name: string, description: string) => void;
}

const NewDeckDialog: React.FC<NewDeckDialogProps> = ({ isOpen, onClose, onCreate }) => {
  const [newDeckName, setNewDeckName] = useState('');
  const [newDeckDescription, setNewDeckDescription] = useState('');

  const handleCreate = () => {
    onCreate(newDeckName, newDeckDescription);
    setNewDeckName('');
    setNewDeckDescription('');
    onClose();
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
          <div className='flex justify-end gap-2'>
            <Button variant='outline' onClick={onClose}>
              Cancel
            </Button>
            <Button onClick={handleCreate} disabled={!newDeckName}>
              Create
            </Button>
          </div>
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default NewDeckDialog;
