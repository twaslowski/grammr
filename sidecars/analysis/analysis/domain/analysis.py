from __future__ import annotations

from pydantic import BaseModel
from spacy.tokens import Token


class Analysis(BaseModel):
    phrase: str
    request_id: str
    tokens: list["AnalysisToken"]


class AnalysisToken(BaseModel):
    text: str
    lemma: str

    @staticmethod
    def from_spacy_token(token: Token) -> AnalysisToken:
        return AnalysisToken(text=token.text, lemma=token.lemma_)
