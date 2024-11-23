import logging
import os
import spacy
from fastapi import FastAPI
from fastapi.responses import JSONResponse

from analysis.domain.analysis_request import AnalysisRequest
from analysis.service import analysis_service

nlp = spacy.load(os.getenv("SPACY_MODEL"))
app = FastAPI()

logger = logging.getLogger(__name__)


@app.post("/morphological-analysis")
async def analyze(request: AnalysisRequest) -> JSONResponse:
    logger.info(f"Received request: {request}")
    analysis = analysis_service.perform_analysis(request)
    logger.info(f"Returning analysis: {analysis.model_dump()}")
    return analysis


@app.get("/health")
async def health():
    return {"status": "UP"}
