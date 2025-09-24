from enum import Enum


class Feature(Enum):
    pass

    def json(self):
        return {"type": self.__class__.__name__.upper(), "value": self.name}


class Case(Feature):
    NOM = "nomn"
    GEN = "gent"
    DAT = "datv"
    ACC = "accs"
    ABL = "ablt"
    LOC = "loct"


class Number(Feature):
    SING = "sing"
    PLUR = "plur"


class Gender(Feature):
    MASC = "masc"
    FEM = "femn"
    NEUT = "neut"


class Person(Feature):
    FIRST = "1per"
    SECOND = "2per"
    THIRD = "3per"


class Tense(Feature):
    PAST = "past"
    PRES = "pres"
    FUT = "futr"
