from enum import Enum


class Feature(Enum):
    pass

    def json(self):
        return {"type": self.__class__.__name__.upper(), "value": self.name}


class Case(Feature):
    NOM = "Nom"
    GEN = "Gen"
    DAT = "Dat"
    ACC = "Acc"


class Number(Feature):
    SING = "Sing"
    PLUR = "Plur"


class Gender(Feature):
    MASC = "Masc"
    FEM = "Fem"
    NEUT = "Neut"


class Person(Feature):
    FIRST = "1"
    SECOND = "2"
    THIRD = "3"


class Tense(Feature):
    PAST = "Past"
    PRES = "Pres"
    FUT = "Fut"
