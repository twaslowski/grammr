import logging

from fastapi import FastAPI
import json

from inflection.domain.inflection import Inflections
from inflection.service import inflector
from inflection.domain.inflection_request import InflectionRequest

logger = logging.getLogger(__name__)
logging.basicConfig(
    level=logging.INFO, format="%(asctime)s - %(name)s - %(levelname)s - %(message)s"
)
app = FastAPI()


@app.post("/inflect")
async def inflect(request: InflectionRequest):
    inflections = inflector.inflect(request.lemma)
    inflections_container = Inflections(
        part_of_speech=request.part_of_speech,
        lemma=request.lemma,
        inflections=inflections,
    )
    result = json.dumps(inflections_container.json())
    logging.info(result)
    return inflections_container.json()


@app.get("/health")
async def health():
    return {"status": "UP"}
