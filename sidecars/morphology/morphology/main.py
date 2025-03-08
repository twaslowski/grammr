import logging
from fastapi import FastAPI
from fastapi.responses import JSONResponse

from morphology.domain.analysis_request import AnalysisRequest
from morphology.service import analysis_service

logger = logging.getLogger(__name__)
logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
app = FastAPI()


@app.post("/morphological-analysis")
async def analyze(request: AnalysisRequest) -> JSONResponse:
    logger.info(f"Received request: {request}")
    analysis = analysis_service.perform_analysis(request)
    logger.info(f"Returning morphology: {analysis.model_dump()}")
    return JSONResponse(
        content=analysis.model_dump(), headers={"Content-Type": "application/json"}
    )


@app.get("/health")
async def health():
    return {"status": "UP"}
