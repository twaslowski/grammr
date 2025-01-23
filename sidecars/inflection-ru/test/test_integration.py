from inflection.service import inflector
from inflection.service import feature_retriever


def test_should_perform_full_inflection_for_verb():
    umet = "уметь"
    part_of_speech = "VERB"
    features = feature_retriever.retrieve_features(part_of_speech)
    inflections = inflector.inflect(umet, features)
    assert len(inflections) == 6


def test_should_perform_full_inflection_for_noun():
    delo = "дело"
    part_of_speech = "NOUN"
    features = feature_retriever.retrieve_features(part_of_speech)
    inflections = inflector.inflect(delo, features)
    assert len(inflections) == 12
