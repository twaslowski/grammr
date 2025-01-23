from inflection.domain.feature import Number, Case
from inflection.domain.inflection import Inflection, Inflections


def test_should_produce_well_formed_json():
    inflection = Inflection(
        lemma="lemma", inflected="lemmo", features={Case.NOM, Number.SING}
    )
    inflections_container = Inflections(lemma="lemma", inflections=[inflection])
    assert inflections_container.json() == {
        "lemma": "lemma",
        "inflections": [
            {
                "lemma": "lemma",
                "inflected": "lemmo",
                "features": [
                    {"type": "NUMBER", "value": "SING"},
                    {"type": "CASE", "value": "NOM"},
                ],
            }
        ],
    }
    print(inflections_container.json())
