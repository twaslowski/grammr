from itertools import product

from inflection.domain.feature import Number, Case, Person, Feature, Gender, \
  Tense


def provide_features(part_of_speech: str) -> list[dict]:
    features = []
    if part_of_speech == "NOUN" or part_of_speech == "ADJ":
        for number, case in product(Number, Case):
            features.append({"Number": number.value, "Case": case.value})
    elif part_of_speech == "VERB" or part_of_speech == "AUX":
        for number, person in product(Number, Person):
            features.append({"Number": number.value, "Person": person.value})
    return features


# todo. make it work now, make it pretty later.
def map_to_standardized_features(features: dict) -> list[Feature]:
    result = []
    for key, value in features.items():
        if key == "Number":
            result.append(Number(value))
        elif key == "Case":
            result.append(Case(value))
        elif key == "Person":
            result.append(Person(value))
        elif key == "Gender":
            result.append(Gender(value))
        elif key == "Tense":
            result.append(Tense(value))
    return result
