import json
import logging

from morphology.domain.analysis_request import AnalysisRequest
from morphology.service import analysis_service
from morphology.lambda_util import ok, check_keep_warm


"""
The split into the lambda_handlers and lambda_handlers_nlp files is unfortunately required.
When importing from the syntactical_analysis module, spaCy gets imported transitively.
For memory reasons, spaCy is only included where required, so its import will fail in the non-dockerized lambdas.
"""

# configure logging
logging.basicConfig(format="%(asctime)s - %(name)s - %(levelname)s - %(message)s")
logger = logging.getLogger("root")
logger.setLevel(logging.INFO)


def handler(event, _) -> dict:
    logger.info(event)
    if pre_warm_response := check_keep_warm(event):
        return pre_warm_response
    body = AnalysisRequest(**json.loads(event.get("body")))
    logger.info(f"Received sentence, language: {body.phrase}")
    analysis = analysis_service.perform_analysis(body)
    return ok(analysis.model_dump())
