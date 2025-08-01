import { useEffect, useState } from 'react';
import { AnalysisV2 } from '@/types/analysis';

export interface TranslationData {
  analysis: AnalysisV2;
  originalPhrase: string;
}

const useTranslation = () => {
  const [translationData, setTranslationData] = useState<TranslationData | null>(null);

  useEffect(() => {
    const storedData = window.sessionStorage.getItem('translationData');
    if (storedData) {
      console.log(storedData);
      setTranslationData(JSON.parse(storedData));
    }
  }, []);

  const saveTranslation = (originalPhrase: string, data: AnalysisV2) => {
    window.sessionStorage.setItem('translationData', JSON.stringify(data));
    setTranslationData({
      analysis: data,
      originalPhrase: originalPhrase,
    });
  };

  const clearTranslation = () => {
    window.sessionStorage.removeItem('translationData');
    setTranslationData(null);
  };

  return {
    translation: translationData,
    saveTranslation: saveTranslation,
    clearTranslation: clearTranslation,
  };
};

export default useTranslation;
