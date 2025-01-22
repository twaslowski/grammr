import logging

import pymorphy3
from pymorphy3.analyzer import Parse
from inflection.service import feature_retriever

from inflection.domain.inflection import Inflection

morph = pymorphy3.MorphAnalyzer()


def inflect(word: str, features: list[set]) -> list[Inflection]:
    parsed = morph.parse(word)[0]
    logging.info(parsed)
    return [_create_inflection(parsed, feature) for feature in features]


def _create_inflection(parsed: Parse, features: set[str]) -> Inflection:
    inflected = parsed.inflect(features)
    features = feature_retriever.map_to_standardized_features(features)
    logging.info(f"Inflected: {inflected.word}, features: {features}")
    return Inflection(
        lemma=parsed.normal_form, inflected=inflected.word, features=features
    )
