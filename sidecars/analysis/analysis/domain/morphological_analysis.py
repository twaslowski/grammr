from __future__ import annotations

from pydantic import BaseModel, Field
from spacy.tokens import Token


class MorphologicalAnalysis(BaseModel):
    source_phrase: str = Field(alias="sourcePhrase")
    request_id: str = Field(alias="requestId")
    tokens: list["TokenMorphology"]


class TokenMorphology(BaseModel):
    text: str
    lemma: str

    @staticmethod
    def from_spacy_token(token: Token) -> TokenMorphology:
        return TokenMorphology(text=token.text, lemma=token.lemma_)
