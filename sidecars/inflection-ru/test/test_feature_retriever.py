import pytest
from fastapi import HTTPException
from inflection.domain.feature import Number, Person
from inflection.domain.inflection_request import InflectionRequest
from inflection.main import _check_processable
from inflection.service import feature_retriever


def test_should_generate_verbal_features():
    part_of_speech = "VERB"
    features = feature_retriever.retrieve_features(part_of_speech)
    # Python apparently cares about collection order,
    # so we sort the list and its contents recursively.
    sorted_features = sorted([sorted(feature) for feature in features])
    expected_features = sorted(
        [
            sorted({"1per", "sing"}),
            sorted({"2per", "sing"}),
            sorted({"3per", "sing"}),
            sorted({"1per", "plur"}),
            sorted({"2per", "plur"}),
            sorted({"3per", "plur"}),
        ]
    )
    assert sorted_features == expected_features


def test_should_generate_noun_features():
    part_of_speech = "NOUN"
    features = feature_retriever.retrieve_features(part_of_speech)
    assert len(features) == 12


def test_should_map_to_standard_features():
    features = {"1per", "sing"}
    standardized_features = feature_retriever.map_to_standardized_features(features)
    assert standardized_features == {Person.FIRST, Number.SING}


def test_should_map_to_enum_entry_if_available():
    value = "1per"
    result = feature_retriever._get_enum_member(Person, value)
    assert result == Person.FIRST


def test_should_return_none_if_enum_entry_not_available():
    value = "foo"
    result = feature_retriever._get_enum_member(Person, value)
    assert result is None


def test_should_raise_http_exception_for_adverb():
    request = InflectionRequest(partOfSpeechTag="ADV", lemma="test")
    with pytest.raises(HTTPException) as exc_info:
        _check_processable(request)
    assert exc_info.value.status_code == 422
    assert exc_info.value.detail == "part-of-speech cannot be processed"
