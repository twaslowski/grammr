import logging

from fastapi import FastAPI
from inflection.service import inflector
from inflection.service import feature_retriever
from inflection.domain.inflection_request import InflectionRequest

logger = logging.getLogger(__name__)
logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
app = FastAPI()


@app.post("/inflect")
async def inflect(request: InflectionRequest):
    features = feature_retriever.retrieve_features(request.part_of_speech)
    return inflector.inflect(request.lemma, features)


@app.get("/health")
async def health():
    return {"status": "UP"}
