from inflection.domain.feature import Person, Number, Feature


def retrieve_features() -> list[set[Feature]]:
    """
    Returns exactly only the possible features for Verbs, since the underlying
    library only conjugates.
    :return: list of sets of features
    """
    return [
        {Person.FIRST, Number.SING},
        {Person.SECOND, Number.SING},
        {Person.THIRD, Number.SING},
        {Person.FIRST, Number.PLUR},
        {Person.SECOND, Number.PLUR},
        {Person.THIRD, Number.PLUR},
    ]


def is_word_inflectable(part_of_speech: str) -> bool:
    return part_of_speech == "VERB"
