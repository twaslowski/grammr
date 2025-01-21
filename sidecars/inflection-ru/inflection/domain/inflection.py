from dataclasses import dataclass


@dataclass
class Inflection:
    lemma: str
    inflected: str
    features: set
