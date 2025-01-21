from itertools import product

from inflection.domain.feature import Person, Number, Case, Gender


def retrieve_features(part_of_speech: str) -> list[set]:
    if part_of_speech == "NOUN" or part_of_speech == "ADJ":
        return [
            {number.value, case.value, gender.value}
            for number, case, gender in product(Number, Case, Gender)
        ]
    elif part_of_speech == "VERB" or part_of_speech == "AUX":
        return [
            {person.value, number.value} for person, number in product(Person, Number)
        ]
