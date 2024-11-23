from __future__ import annotations

from pydantic import BaseModel
from spacy.tokens import Token


class MorphologicalAnalysis(BaseModel):
    source_phrase: str
    request_id: str
    tokens: list["TokenMorphology"]


class TokenMorphology(BaseModel):
    text: str
    lemma: str
    pos: str

    @staticmethod
    def from_spacy_token(token: Token) -> TokenMorphology:
        return TokenMorphology(text=token.text, lemma=token.lemma_, pos=token.pos_)
