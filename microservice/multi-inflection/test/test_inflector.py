import pytest
from fastapi import HTTPException
from inflection.domain.inflection_request import InflectionRequest
from inflection.domain.feature import Number, Person
from inflection.main import _check_processable
from inflection.service import inflector


def test_should_conjugate_verb():
    lemma = "essere"
    inflections = inflector.inflect(lemma)
    assert len(inflections) == 6
    assert inflections[0].inflected == "io sono"
    assert inflections[0].features == {Person.FIRST, Number.SING}
    assert inflections[1].inflected == "tu sei"


def test_should_raise_http_exception_for_noun():
    request = InflectionRequest(partOfSpeechTag="NOUN", lemma="test")
    with pytest.raises(HTTPException) as exc_info:
        _check_processable(request)
    assert exc_info.value.status_code == 422
    assert exc_info.value.detail == "part-of-speech cannot be processed"
