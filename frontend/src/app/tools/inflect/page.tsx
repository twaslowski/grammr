'use client';

import React, { useState } from 'react';
import { InputArea } from '@/components/common/InputArea';
import InflectionTable from '@/inflection/components/InflectionTable';
import { useApi } from '@/hooks/useApi';
import { Paradigm } from '@/flashcard/types/paradigm';
import Error from '@/components/common/Error';

const InflectionPage = () => {
  const { request, error, isLoading } = useApi();
  const [paradigm, setParadigm] = useState<Paradigm | null>(null);

  const inflect = async (text: string) => {
    setParadigm(null);
    const result = await request<Paradigm>('/api/v1/inflection', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        lemma: text,
        partOfSpeechTag: 'VERB',
        languageCode: 'RU',
      }),
    });
    setParadigm(result);
  };

  return (
    <main>
      <div className='px-4 py-8 max-w-3xl mx-auto'>
        <InputArea onEnter={inflect} clear={false} />

        <div>
          {paradigm && (
            <InflectionTable inflections={paradigm} error={error} isLoading={isLoading} />
          )}
        </div>
      </div>
    </main>
  );
};

export default InflectionPage;
