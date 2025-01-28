import spacy

from derbi.derbi import DERBI

from inflection.domain.inflection import Inflection
from inflection.service import feature_provider

nlp = spacy.load("de_core_news_sm")
derbi = DERBI(nlp)


def inflect(word: str, features: list[dict]) -> list[Inflection]:
    inflected = []
    for feature in features:
        output = derbi(word, feature)
        f = feature_provider.map_to_standardized_features(output["target_tags"])
        inflected.append(Inflection(
            lemma=output["token"].text,
            inflected=output["result"],
            features=f
        ))
    return inflected
