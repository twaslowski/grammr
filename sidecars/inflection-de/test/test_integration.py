from inflection.service import feature_provider, inflector


def test_should_create_inflections():
    word = "gehen"
    pos = "VERB"
    features = feature_provider.provide_features(pos)
    inflections = inflector.inflect(word, features)
    assert len(inflections) == 6


def test_should_decline_noun():
    word = "Hund"
    pos = "NOUN"
    features = feature_provider.provide_features(pos)
    inflections = inflector.inflect(word, features)
    assert len(inflections) == 8
