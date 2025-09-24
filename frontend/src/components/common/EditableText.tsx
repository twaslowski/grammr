'use client';

import { Edit2, Check, X } from 'lucide-react';
import { useState, useEffect, useRef } from 'react';
import { Button } from '@/components/ui/button';

interface EditableTextProps {
  value: string;
  onSaveAction: (newValue: string) => Promise<void>;
  className?: string;
  placeholder?: string;
  multiline?: boolean;
}

export default function EditableText({
  value,
  onSaveAction,
  className = '',
  placeholder = 'Enter text...',
  multiline = false,
}: EditableTextProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [editValue, setEditValue] = useState(value);
  const [isSaving, setIsSaving] = useState(false);

  useEffect(() => {
    setEditValue(value);
  }, [value]);

  const inputRef = useRef<HTMLInputElement | HTMLTextAreaElement>(null);

  useEffect(() => {
    if (isEditing && inputRef.current) {
      inputRef.current.focus();
      // Select all text when entering edit mode
      if (inputRef.current instanceof HTMLInputElement) {
        inputRef.current.select();
      } else if (inputRef.current instanceof HTMLTextAreaElement) {
        inputRef.current.setSelectionRange(0, inputRef.current.value.length);
      }
    }
  }, [isEditing]);

  const handleEdit = () => {
    setIsEditing(true);
    setEditValue(value);
  };

  const handleSave = async () => {
    if (editValue.trim() !== value && !isSaving) {
      setIsSaving(true);
      try {
        await onSaveAction(editValue.trim());
      } catch (error) {
        // Reset to original value on error
        setEditValue(value);
      } finally {
        setIsSaving(false);
      }
    }
    setIsEditing(false);
  };

  const handleCancel = () => {
    setEditValue(value);
    setIsEditing(false);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey && !multiline) {
      e.preventDefault();
      handleSave();
    } else if (e.key === 'Escape') {
      e.preventDefault();
      handleCancel();
    }
  };

  if (isEditing) {
    const InputComponent = multiline ? 'textarea' : 'input';

    return (
      <div className='flex items-start space-x-2'>
        <InputComponent
          ref={inputRef as any}
          type={multiline ? undefined : 'text'}
          value={editValue}
          onChange={(e) => setEditValue(e.target.value)}
          onKeyDown={handleKeyDown}
          onBlur={handleSave}
          placeholder={placeholder}
          disabled={isSaving}
          className={`flex-1 px-2 py-1 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent ${className}`}
          rows={multiline ? 3 : undefined}
        />
        <div className='flex space-x-1'>
          <Button
            size='sm'
            variant='ghost'
            onClick={handleSave}
            disabled={isSaving}
            className='h-6 w-6 p-0 text-green-600 hover:text-green-800'
          >
            <Check size={14} />
          </Button>
          <Button
            size='sm'
            variant='ghost'
            onClick={handleCancel}
            disabled={isSaving}
            className='h-6 w-6 p-0 text-red-600 hover:text-red-800'
          >
            <X size={14} />
          </Button>
        </div>
      </div>
    );
  }

  return (
    <div className='group flex items-center space-x-2'>
      <span className={className}>{value || placeholder}</span>
      <Button
        size='sm'
        variant='ghost'
        onClick={handleEdit}
        className='h-6 w-6 p-0 opacity-0 group-hover:opacity-100 transition-opacity text-gray-500 hover:text-gray-700'
      >
        <Edit2 size={14} />
      </Button>
    </div>
  );
}
