export interface Feature {
  type: string;
  value: string;
  fullIdentifier: string;
}

export enum FeatureType {
  CASE = 'Case',
  PERSON = 'Person',
  NUMBER = 'Number',
  TENSE = 'Tense',
  GENDER = 'Gender',
  MISC = 'Undefined',
}

export enum Case {
  NOM = 'Nominative',
  GEN = 'Genitive',
  DAT = 'Dative',
  ACC = 'Accusative',
  ABL = 'Ablative',
  VOC = 'Vocative',
  LOC = 'Locative',
  INS = 'Instrumental',
}

export enum Person {
  FIRST = 'First person',
  SECOND = 'Second person',
  THIRD = 'Third person',
}

export enum Number {
  SINGULAR = 'Singular',
  PLURAL = 'Plural',
}

export enum Tense {
  PAST = 'Past tense',
  IMP = 'Imperfect',
  PRES = 'Present tense',
  PQP = 'Plusquamperfect',
  FUT = 'Future tense',
}

export enum Gender {
  MASC = 'Masculine',
  FEM = 'Feminine',
  NEUT = 'Neuter',
}
