import spacy

from morphology.service import feature_extraction

nlp = spacy.load("de_core_news_sm")


def test_should_extract_features():
    text = "Der schnelle braune Fuchs springt über den faulen Hund."
    doc = nlp(text)

    der = doc[0]
    features = feature_extraction.extract_features(der)
    assert find_feature_by_type("case", features).value == "NOM"
    assert find_feature_by_type("number", features).value == "SING"
    assert find_feature_by_type("gender", features).value == "MASC"

    springt = doc[4]
    features = feature_extraction.extract_features(springt)
    assert find_feature_by_type("person", features).value == "3"
    assert find_feature_by_type("number", features).value == "SING"
    assert find_feature_by_type("tense", features).value == "PRES"


def find_feature_by_type(
    type: str, features: list[feature_extraction.Feature]
) -> str | None:
    for feature in features:
        if feature.type.lower() == type.lower():
            return feature
    return None
