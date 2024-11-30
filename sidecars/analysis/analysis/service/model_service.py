import logging
import os
import spacy

from fastapi import HTTPException

__models_map = {}


def construct_models_map():
    for model_name in os.environ["SPACY_MODELS"].split(","):
        language_code = _extract_language_code(model_name)
        __models_map[language_code.upper()] = spacy.load(model_name)
        logging.info(f"Loaded model for language code {language_code}")


def retrieve_model_for_language_code(language_code: str):
    try:
        return __models_map[language_code.upper()]
    except KeyError:
        raise HTTPException(
            status_code=422,
            detail="Morphological analysis for this language is not supported",
        )


def _extract_language_code(model_name: str):
    return model_name[:2]
