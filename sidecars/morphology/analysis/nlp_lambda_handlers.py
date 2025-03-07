import json
import logging

from analysis.domain.analysis_request import AnalysisRequest
from analysis.service import analysis_service
from analysis.lambda_util import ok, check_pre_warm


"""
The split into the lambda_handlers and lambda_handlers_nlp files is unfortunately required.
When importing from the syntactical_analysis module, spaCy gets imported transitively.
For memory reasons, spaCy is only included where required, so its import will fail in the non-dockerized lambdas.
"""

# configure logging
logging.basicConfig(format="%(asctime)s - %(name)s - %(levelname)s - %(message)s")
logger = logging.getLogger("root")
logger.setLevel(logging.INFO)


def syntactical_analysis_handler(event, _) -> dict:
    if pre_warm_response := check_pre_warm(event):
        return pre_warm_response
    body = AnalysisRequest(**json.loads(event.get("body")))
    logger.info(f"Received sentence, language: {body.phrase}")
    analysis = analysis_service.perform_analysis(body)
    return ok(analysis.model_dump())

