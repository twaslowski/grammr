from itertools import product

from inflection.domain.feature import Number, Case, Person


def provide_features(part_of_speech: str) -> list[dict]:
    features = []
    if part_of_speech == "NOUN" or part_of_speech == "ADJ":
        for number, case in product(Number, Case):
            features.append({"Number": number.value, "Case": case.value})
    elif part_of_speech == "VERB" or part_of_speech == "AUX":
        for number, person in product(Number, Person):
            features.append({"Number": number.value, "Person": person.value})
    return features
