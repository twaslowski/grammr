import os
import spacy

from morphology.service.token_mapper import from_spacy_token
from morphology.domain.analysis_request import AnalysisRequest
from morphology.domain.morphological_analysis import (
    MorphologicalAnalysis,
)


model = spacy.load(os.getenv("SPACY_MODEL"))


def perform_analysis(request: AnalysisRequest) -> list:
    spacy_tokens = model(request.phrase)
    return MorphologicalAnalysis(
        source_phrase=request.phrase,
        request_id=request.request_id,
        tokens=[
            from_spacy_token(token)
            for token in spacy_tokens
            if not _token_is_punctuation(token)
        ],
    )


def keep_warm():
    global model
    model = spacy.load(os.getenv("SPACY_MODEL"))


def _token_is_punctuation(token):
    return token.pos_ == "PUNCT"
