import logging
import os
import spacy

from fastapi import HTTPException

__models_map = {}


def _extract_language_code(model_name: str):
    return model_name[:2]
