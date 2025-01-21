from enum import Enum


class Case(Enum):
    NOMINATIVE = "nomn"
    GENITIVE = "gent"
    DATIVE = "datv"
    ACCUSATIVE = "accs"
    INSTRUMENTAL = "ablt"
    LOCATIVE = "loct"


class Number(Enum):
    SINGULAR = "sing"
    PLURAL = "plur"


class Gender(Enum):
    MASCULINE = "masc"
    FEMININE = "femn"
    NEUTER = "neut"


class Person(Enum):
    FIRST = "1per"
    SECOND = "2per"
    THIRD = "3per"


class Tense(Enum):
    PAST = "past"
    PRESENT = "pres"
    FUTURE = "futr"
