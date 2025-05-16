import { useEffect, useState } from 'react';
import Analysis from '@/types/analysis';

const useAnalysis = () => {
  const [translationData, setTranslationData] = useState<Analysis | null>(null);

  useEffect(() => {
    const storedData = window.sessionStorage.getItem('translationData');
    if (storedData) {
      setTranslationData(JSON.parse(storedData));
    }
  }, []);

  const saveTranslation = (data: Analysis) => {
    window.sessionStorage.setItem('translationData', JSON.stringify(data));
    setTranslationData(data);
  };

  const clearTranslation = () => {
    window.sessionStorage.removeItem('translationData');
    setTranslationData(null);
  };

  return {
    analysis: translationData,
    saveAnalysis: saveTranslation,
    clearAnalysis: clearTranslation,
  };
};

export default useAnalysis;
