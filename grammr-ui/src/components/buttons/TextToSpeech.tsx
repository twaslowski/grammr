'use client';

import { Loader, Volume2 } from 'lucide-react';
import { useEffect, useRef, useState } from 'react';
import * as React from 'react';

interface TTSPlayerProps {
  text: string;
}

export default function TTSPlayer({ text }: TTSPlayerProps) {
  const [isLoading, setIsLoading] = useState(false);
  const [isTooltipVisible, setIsTooltipVisible] = useState(false);
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

      // Otherwise fetch from API
      const response = await fetch('/api/v1/speak', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({ text }),
      });

      if (!response.ok) {
        throw new Error(`API error: ${response.status}`);
      }

      // Get the readable stream from the response
      const reader = response.body!.getReader();
      const chunks: Uint8Array[] = [];

      // Read the stream

      while (true) {
        const { done, value } = await reader.read();
        if (done) break;
        chunks.push(value);
      }

      // Combine all chunks into a single Blob
      const blob = new Blob(chunks, { type: 'audio/mpeg' });

      // Cache the audio for future use
      audioCache.current.set(text, blob);

      // Play the audio
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
    audio.play();
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
}
