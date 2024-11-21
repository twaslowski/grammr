from __future__ import annotations

from pydantic import BaseModel, Field
from spacy.tokens import Token


class GrammaticalAnalysis(BaseModel):
    source_phrase: str = Field(alias="sourcePhrase")
    request_id: str = Field(alias="requestId")
    tokens: list["AnalysisToken"]


class AnalysisToken(BaseModel):
    text: str
    lemma: str

    @staticmethod
    def from_spacy_token(token: Token) -> AnalysisToken:
        return AnalysisToken(text=token.text, lemma=token.lemma_)
