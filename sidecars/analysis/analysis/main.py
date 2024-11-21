import logging
import os
import spacy
from fastapi import FastAPI
from fastapi.responses import JSONResponse
from spacy.tokens import Token

from analysis.domain.grammatical_analysis import MorphologicalAnalysis, TokenMorphology
from analysis.domain.analysis_request import AnalysisRequest

nlp = spacy.load(os.getenv("SPACY_MODEL"))
app = FastAPI()

logger = logging.getLogger(__name__)


@app.post("/morphological-analysis")
async def analyze(request: AnalysisRequest) -> JSONResponse:
    logger.info(f"Received request: {request}")
    tokens = _perform_analysis(request.phrase)
    analysis = _create_response(tokens, request)
    logger.info(f"Returning analysis: {analysis.model_dump()}")
    return analysis


@app.get("/health")
async def health():
    return {"status": "UP"}


def _perform_analysis(phrase: str) -> list:
    doc = nlp(phrase)
    return [token for token in doc if token.pos_ != "PUNCT"]


def _create_response(tokens: list[Token], request: AnalysisRequest) -> MorphologicalAnalysis:
  # todo: ideally, the aliasing should work better so I can entirely avoid camelcase.
  # maybe this serialization logic can be moved to jackson in the Java code
    return MorphologicalAnalysis(
        sourcePhrase=request.phrase,
        requestId=request.request_id,
        tokens=[TokenMorphology.from_spacy_token(token) for token in tokens],
    )


if __name__ == "__main__":
    pass
