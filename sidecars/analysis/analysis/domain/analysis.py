from __future__ import annotations

from pydantic import BaseModel


class Analysis(BaseModel):
  phrase: str
  request_id: str
  token: "AnalysisToken"


class AnalysisToken(BaseModel):
  text: str
  lemma: str
