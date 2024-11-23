import os
import spacy

from analysis.domain.analysis_request import AnalysisRequest
from analysis.domain.morphological_analysis import (
    MorphologicalAnalysis,
    TokenMorphology,
)

nlp = spacy.load(os.getenv("SPACY_MODEL"))


def perform_analysis(request: AnalysisRequest) -> list:
    spacy_tokens = nlp(request.phrase)
    return MorphologicalAnalysis(
        source_phrase=request.phrase,
        request_id=request.request_id,
        tokens=[
            TokenMorphology.from_spacy_token(token)
            for token in spacy_tokens
            if not _token_is_punctuation(token)
        ],
    )


def _token_is_punctuation(token):
    return token.pos_ == "PUNCT"
