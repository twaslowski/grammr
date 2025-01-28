from pydantic import BaseModel

from inflection.domain.feature import Feature


class Inflection(BaseModel):
    lemma: str
    inflected: str
    features: set[Feature]

    def json(self, **kwargs) -> dict:
        return {
            "lemma": self.lemma,
            "inflected": self.inflected,
            "features": [feature.json() for feature in self.features],
        }

    @staticmethod
    def from_derbi(derbi_output: dict):
        return Inflection(
            lemma=derbi_output["token"].text,
            inflected=derbi_output["result"],
            features=derbi_output["target_tags"],
        )


class Inflections(BaseModel):
    lemma: str
    inflections: list[Inflection]

    def json(self, **kwargs) -> dict:
        return {
            "lemma": self.lemma,
            "inflections": [inflection.json() for inflection in self.inflections],
        }
