import { useEffect, useState } from 'react';
import { AnalysisV2 } from '@/types/analysis';

const useAnalysis = () => {
  const [translationData, setTranslationData] = useState<AnalysisV2 | null>(null);

  useEffect(() => {
    const storedData = window.sessionStorage.getItem('translationData');
    if (storedData) {
      setTranslationData(JSON.parse(storedData));
    }
  }, []);

  const saveTranslation = (data: AnalysisV2) => {
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
