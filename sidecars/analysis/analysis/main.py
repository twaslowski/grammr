import os
import spacy
from fastapi import FastAPI
from spacy.tokens import Token

from analysis.domain.analysis import Analysis, AnalysisToken
from analysis.domain.analysis_request import AnalysisRequest

nlp = spacy.load(os.getenv("SPACY_MODEL"))
app = FastAPI()


@app.post("/analysis")
async def analyze(request: AnalysisRequest) -> Analysis:
    tokens = _perform_analysis(request.phrase)
    print(tokens)
    analysis = _create_response(tokens, request)
    return analysis


@app.get("/health")
async def health():
    return {"status": "UP"}


def _perform_analysis(phrase: str) -> list:
    doc = nlp(phrase)
    return [token for token in doc if token.pos_ != "PUNCT"]


def _create_response(tokens: list[Token], request: AnalysisRequest) -> Analysis:
    return Analysis(
        phrase=request.phrase,
        request_id=request.request_id,
        tokens=[AnalysisToken.from_spacy_token(token) for token in tokens],
    )


if __name__ == "__main__":
    pass
