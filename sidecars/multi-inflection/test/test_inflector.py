from inflection.domain.feature import Number, Person
from inflection.service import inflector


def test_should_conjugate_verb():
    lemma = "essere"
    inflections = inflector.inflect(lemma)
    assert len(inflections) == 6
    assert inflections[0].inflected == "io sono"
    assert inflections[0].features == {Person.FIRST, Number.SING}
    assert inflections[1].inflected == "tu sei"
