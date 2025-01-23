from inflection.service import inflector


def test_should_generate_single_inflection_for_infinitive_verb():
    umet = "уметь"
    inflections = inflector.inflect(umet, [{"1per", "sing"}])
    assert len(inflections) == 1
    assert inflections[0].lemma == "уметь"
    assert inflections[0].inflected == "умею"


def test_should_generate_several_inflections():
    umet = "уметь"
    inflections = inflector.inflect(
        umet, [{"1per", "sing"}, {"2per", "sing"}, {"3per", "sing"}, {"1per", "plur"}]
    )
    assert len(inflections) == 4

    assert inflections[0].lemma == "уметь"
    assert inflections[0].inflected == "умею"

    assert inflections[1].lemma == "уметь"
    assert inflections[1].inflected == "умеешь"

    assert inflections[2].lemma == "уметь"
    assert inflections[2].inflected == "умеет"

    assert inflections[3].lemma == "уметь"
    assert inflections[3].inflected == "умеем"
