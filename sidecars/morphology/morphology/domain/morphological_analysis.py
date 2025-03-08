from __future__ import annotations

from pydantic import BaseModel


class MorphologicalAnalysis(BaseModel):
    source_phrase: str
    request_id: str
    tokens: list["TokenMorphology"]


class TokenMorphology(BaseModel):
    text: str
    lemma: str
    pos: str
    features: list["Feature"] = []


class Feature(BaseModel):
    type: str
    value: str
