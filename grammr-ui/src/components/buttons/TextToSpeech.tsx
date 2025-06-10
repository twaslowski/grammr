'use client';

import { Loader, Volume2 } from 'lucide-react';
import * as React from 'react';
import { useEffect, useRef, useState } from 'react';
import { useLanguage } from '@/context/LanguageContext';

export const TextToSpeech: React.FC<{ text: string }> = ({ text }) => {
  const { languageLearned } = useLanguage();
  const [isLoading, setIsLoading] = useState(false);
  const [_, setIsTooltipVisible] = useState(false);
  const audioCache = useRef<Map<string, Blob>>(new Map());
  const currentAudio = useRef<HTMLAudioElement | null>(null);

  // Clean up audio elements when component unmounts
  useEffect(() => {
    return () => {
      if (currentAudio.current) {
        currentAudio.current.pause();
      }
    };
  }, []);

  const speakText = async () => {
    if (!text.trim()) return;

    setIsLoading(true);

    try {
      // Check if we have the audio cached
      if (audioCache.current.has(text)) {
        playAudioBlob(audioCache.current.get(text)!);
        setIsLoading(false);
        return;
      }

      // Otherwise fetch from the new TTS API
      const response = await fetch(`/api/tts`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          text: text,
          language: languageLearned,
        }),
      });

      if (!response.ok) {
        throw new Error(`API error: ${response.status}`);
      }

      const base64 = await response.text();

      // Decode base64 to binary
      const binary = atob(base64);
      const len = binary.length;
      const bytes = new Uint8Array(len);
      for (let i = 0; i < len; i++) {
        bytes[i] = binary.charCodeAt(i);
      }

      // Create Blob from binary data
      const blob = new Blob([bytes], { type: 'audio/mpeg' });
      audioCache.current.set(text, blob);
      playAudioBlob(blob);
    } catch (error) {
      console.error('Error fetching audio:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const playAudioBlob = (blob: Blob) => {
    // Stop any currently playing audio
    if (currentAudio.current) {
      currentAudio.current.pause();
    }

    // Create a URL for the blob
    const audioUrl = URL.createObjectURL(blob);

    // Create and play audio element
    const audio = new Audio(audioUrl);
    audio.onended = () => {
      URL.revokeObjectURL(audioUrl); // Clean up the URL object
    };

    currentAudio.current = audio;
    void audio.play();
  };

  return (
    <div className='flex flex-col space-y-4 w-full max-w-md'>
      <button
        onClick={speakText}
        disabled={isLoading || !text.trim()}
        onMouseEnter={() => setIsTooltipVisible(true)}
        onMouseLeave={() => setIsTooltipVisible(false)}
        className='px-4 py-2 rounded-md text-gray-500 hover:text-gray-700 cursor-pointer transition-colors'
      >
        {isLoading ? (
          <>
            <Loader className='mr-2 animate-spin' />
          </>
        ) : (
          <>
            <Volume2 className='mr-2' />
          </>
        )}
      </button>
    </div>
  );
};
