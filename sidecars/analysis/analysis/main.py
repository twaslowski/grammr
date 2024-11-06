import os

import spacy
from fastapi import FastAPI

from analysis.domain.analysis import Analysis
from analysis.domain.analysis_request import AnalysisRequest

app = FastAPI()
nlp = spacy.load(os.getenv("MODEL"))


@app.post("/analysis")
async def analyze(request: AnalysisRequest):
  return {"message": "Analysis received", "sentence": request.phrase}


def _perform_analysis(phrase: str) -> list:
  doc = nlp(phrase)
  return [token for token in doc if token.pos_ != "PUNCT"]


def _map_to_response_event(tokens: list) -> Analysis:
  return {"translatedTokens": tokens}


if __name__ == '__main__':
  pass
