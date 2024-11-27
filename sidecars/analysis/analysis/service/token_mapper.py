from spacy.tokens import Token

from analysis.domain.morphological_analysis import TokenMorphology
from analysis.service.feature_extraction import extract_features


def from_spacy_token(token: Token) -> TokenMorphology:
    return TokenMorphology(
        text=token.text,
        lemma=token.lemma_,
        pos=token.pos_,
        features=extract_features(token),
    )