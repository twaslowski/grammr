import logging
import os

from verbecc import Conjugator

from verbecc.localization import xtense, xmood

from inflection.domain.inflection import Inflection
from inflection.service import feature_retriever


logger = logging.getLogger(__name__)
LANG = os.environ["LANGUAGE_CODE"]

if LANG not in ["it", "fr", "es", "pt", "ro"]:
    raise ValueError("Language not supported")

logger.info(f"Downloading model for language: {LANG}")
conjugator = Conjugator(lang=LANG)


def inflect(lemma: str) -> list[Inflection]:
    features = feature_retriever.retrieve_features()
    inflections = xconj(lemma)
    logger.info(f"Inflections for lemma {lemma}: {inflections}")
    result = []
    for feature, inflected in zip(features, inflections):
        result.append(Inflection(lemma=lemma, inflected=inflected, features=feature))
    return result


def xconj(lemma: str, mood="indicative", tense="present"):
    return conjugator.conjugate(lemma)["moods"][xmood(LANG, mood)][xtense(LANG, tense)]
