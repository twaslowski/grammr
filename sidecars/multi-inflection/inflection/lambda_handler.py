import json
import logging

from inflection.main import inflect
from fastapi import FastAPI, HTTPException


# configure logging
logging.basicConfig(format="%(levelname)s - %(message)s")
logger = logging.getLogger("root")
logger.setLevel(logging.INFO)


def handler(event, _) -> dict:
    logger.info(event)
    if keep_warm_response := check_keep_warm(event):
        return keep_warm_response
    try:
        request = InflectionRequest(**json.loads(event.get("body")))
        result = inflect(request)
        return ok(result)
    except HTTPException as e:
    except Exception as e:
        logger.error("An error occurred: ", e)
        return fail(500)
