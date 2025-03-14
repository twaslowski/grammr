import logging

from fastapi import FastAPI, HTTPException
import json

from inflection.domain.inflection import Inflections
from inflection.service import inflector
from inflection.domain.inflection_request import InflectionRequest
from inflection.service import feature_retriever

logger = logging.getLogger(__name__)
logging.basicConfig(
    level=logging.INFO, format="%(levelname)s - %(message)s"
)
app = FastAPI()


@app.post("/inflect")
async def inflect(request: InflectionRequest):
    _check_processable(request)
    inflections = inflector.inflect(request.lemma)
    inflections_container = Inflections(
        part_of_speech=request.part_of_speech,
        lemma=request.lemma,
        inflections=inflections,
    )
    result = json.dumps(inflections_container.json())
    logging.info(result)
    return inflections_container.json()


def _check_processable(request: InflectionRequest):
    if not feature_retriever.is_word_inflectable(request.part_of_speech):
        raise HTTPException(
            status_code=422, detail="part-of-speech cannot be processed"
        )


@app.get("/health")
async def health():
    return {"status": "UP"}
