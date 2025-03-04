import json

from inflection.domain.inflection import Inflection


def stringify_inflections(inflections: list[Inflection]) -> str:
    return json.dumps([inflection.model_dump() for inflection in inflections])
