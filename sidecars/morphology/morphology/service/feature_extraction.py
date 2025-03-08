from morphology.domain.morphological_analysis import Feature
from spacy.tokens import Token


def extract_features(token: Token) -> list[Feature]:
    """
    Extracts a dict of features from the universal type tags of a Token.
    :param token: A spaCy token with a token.morph string like "Case=Nom|Number=Plur"
    :return: The features, e.g. {'Case': 'Nom', 'Number': 'Plur'}
    """
    tags = str(token.morph).split("|")
    features = []
    for tag in tags:
        if tag != "":
            feature_type, value = tag.split("=")
            features.append(Feature(type=feature_type.lower(), value=value.upper()))
    return features
