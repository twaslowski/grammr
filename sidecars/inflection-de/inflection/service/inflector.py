import spacy

from derbi.derbi import DERBI

from inflection.domain.inflection import Inflection

nlp = spacy.load("de_core_news_sm")
derbi = DERBI(nlp)


def inflect(word: str, features: list[dict]) -> list[Inflection]:
    inflected = []
    for feature in features:
        # inflected.append(derbi(word, feature))
        inflected.append(Inflection.from_derbi(derbi_output=derbi(word, feature)))
    return inflected
