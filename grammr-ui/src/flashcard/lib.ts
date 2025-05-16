import TokenType from '@/types/tokenType';

export const createTokenFlashcard = async (
  deckId: number,
  token: TokenType,
  paradigmId: string | undefined,
) => {
  console.log(deckId, token, paradigmId);
  return await fetch('/api/v1/anki/flashcard', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      deckId: deckId,
      question: token.morphology.lemma,
      answer: token.translation.source,
      tokenPos: token.morphology.pos,
      paradigmId: paradigmId,
    }),
  });
};
