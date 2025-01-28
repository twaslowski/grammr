from inflection.service import feature_provider


def test_feature_provider_permutations_for_verbs():
    features = feature_provider.provide_features("VERB")
    assert len(features) == 6
    assert {"Number": "Sing", "Person": "1"} in features
    assert {"Number": "Plur", "Person": "1"} in features
    assert {"Number": "Sing", "Person": "2"} in features


def test_feature_provider_permutations_for_nouns():
    features = feature_provider.provide_features("NOUN")
    assert len(features) == 8
    assert {"Case": "Nom", "Number": "Plur"} in features
