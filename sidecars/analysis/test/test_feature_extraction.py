import spacy

from analysis.service import feature_extraction

nlp = spacy.load("de_core_news_sm")


def test_should_extract_features():
    text = "Der schnelle braune Fuchs springt über den faulen Hund."
    doc = nlp(text)

    der = doc[0]
    features = feature_extraction.extract_features(der)
    assert features["gender"] == "MASC"
    assert features["number"] == "SING"
    assert features["case"] == "NOM"

    springt = doc[4]
    features = feature_extraction.extract_features(springt)
    assert features["person"] == "3"
    assert features["number"] == "SING"
    assert features["tense"] == "PRES"
