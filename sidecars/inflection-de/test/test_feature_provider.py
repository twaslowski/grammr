from inflection.domain.feature import Number, Person
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


def test_feature_provider_should_map_dict_to_feature_list():
    features = {"Number": "Sing", "Person": "1"}
    result = feature_provider.map_to_standardized_features(features)
    assert Number.SING in result
    assert Person.FIRST in result
