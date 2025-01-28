from fastapi import FastAPI

import json
import logging

from inflection.domain.inflection import Inflections
from inflection.domain.inflection_request import InflectionRequest
from inflection.service import inflector
from inflection.service import feature_provider


# @app.post("/inflect")
async def inflect(request: InflectionRequest):
    features = feature_provider.provide_features(request.part_of_speech)
    inflections = inflector.inflect(request.lemma, features)
    inflections_container = Inflections(lemma=request.lemma, inflections=inflections)
    result = json.dumps(inflections_container.json())
    logging.info(result)
    return inflections_container.json()


# @app.get("/health")
async def health():
    return {"status": "UP"}
