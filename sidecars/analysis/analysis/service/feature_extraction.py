from spacy.tokens import Token


def extract_features(token: Token) -> dict[str, str]:
    """
    Extracts a dict of features from the universal feature tags of a Token.
    :param token: A spaCy token with a token.morph string like "Case=Nom|Number=Plur"
    :return: The features, e.g. {'Case': 'Nom', 'Number': 'Plur'}
    """
    tags = str(token.morph).split("|")
    result = {}
    for tag in tags:
        if tag != "":
            key, value = tag.split("=")
            result[key.upper()] = value.upper()
    return result
