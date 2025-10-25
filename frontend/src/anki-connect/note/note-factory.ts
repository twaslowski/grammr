import { Flashcard } from '@/flashcard/types/flashcard';
import { MODEL_NAMES } from '@/anki-connect/model/model-names';
import { AnyNote, BasicNote, ConjugationNote, DeclensionNote, InflectionNote } from './type';
import { extractVerbForms } from './conjugation-utils';
import { extractCases } from '@/anki-connect/note/declension-utils';
import { Paradigm } from '@/flashcard/types/paradigm';

export class NoteFactory {
  static createFromFlashcard(flashcard: Flashcard, deckName: string): AnyNote {
    if (flashcard.paradigm) {
      const noteType = this.getInflectionNoteType(flashcard.paradigm);

      switch (noteType) {
        case 'conjugation':
          return this.createConjugation(flashcard, deckName);
        case 'declension':
          return this.createDeclension(flashcard, deckName);
        default:
          return this.createInflection(flashcard, deckName);
      }
    } else {
      return this.createBasic(flashcard, deckName);
    }
  }

  static createBasic(flashcard: Flashcard, deckName: string): BasicNote {
    return {
      id: flashcard.id,
      fields: {
        id: flashcard.id,
        front: flashcard.question,
        back: flashcard.answer,
        notes: '',
      },
      modelName: MODEL_NAMES.BASIC,
      deckName: deckName,
    };
  }

  static createConjugation(flashcard: Flashcard, deckName: string): ConjugationNote {
    if (!flashcard.paradigm) {
      throw new Error('Conjugation note requires paradigm data');
    }
    const paradigm = flashcard.paradigm;
    const flattened = extractVerbForms(paradigm);

    return {
      id: flashcard.id,
      deckName: deckName,
      modelName: MODEL_NAMES.CONJUGATION,
      fields: {
        id: flashcard.id,
        front: flashcard.question,
        back: flashcard.answer,
        translation: flashcard.answer,
        lemma: paradigm.lemma,
        firstPersonSingular: flattened.firstPersonSingular,
        secondPersonSingular: flattened.secondPersonSingular,
        thirdPersonSingular: flattened.thirdPersonSingular,
        firstPersonPlural: flattened.firstPersonPlural,
        secondPersonPlural: flattened.secondPersonPlural,
        thirdPersonPlural: flattened.thirdPersonPlural,
        // TODO: Extract these. Will require more work on the inflections modules.
        mood: 'indicative',
        tense: 'present',
        voice: 'active',
        notes: '',
      },
      tags: [`conjugation`, `${paradigm.languageCode}`],
    };
  }

  static createDeclension(flashcard: Flashcard, deckName: string): DeclensionNote {
    if (!flashcard.paradigm) {
      throw new Error('Declension note requires paradigm data');
    }
    const cases = extractCases(flashcard.paradigm);

    return {
      id: flashcard.id,
      fields: {
        id: flashcard.id,
        front: flashcard.question,
        back: flashcard.answer,
        lemma: flashcard.paradigm.lemma,
        translation: flashcard.answer,
        nominativeSingular: cases.nominativeSingular,
        genitiveSingular: cases.genitiveSingular,
        dativeSingular: cases.dativeSingular,
        accusativeSingular: cases.accusativeSingular,
        nominativePlural: cases.nominativePlural,
        genitivePlural: cases.genitivePlural,
        dativePlural: cases.dativePlural,
        accusativePlural: cases.accusativePlural,
        notes: '',
      },
      modelName: MODEL_NAMES.DECLENSION,
      deckName: deckName,
      tags: [`conjugation`, `${flashcard.paradigm.languageCode}`],
    };
  }

  static createInflection(flashcard: Flashcard, deckName: string): InflectionNote {
    if (!flashcard.paradigm) {
      throw new Error('Inflection note requires paradigm data');
    }

    return {
      id: flashcard.id,
      fields: {
        id: flashcard.id,
        front: flashcard.question,
        back: flashcard.answer,
        lemma: flashcard.paradigm.lemma,
        translation: flashcard.answer,
        table: '',
        notes: '',
      },
      modelName: MODEL_NAMES.INFLECTION_GENERIC,
      deckName: deckName,
    };
  }

  private static getInflectionNoteType(
    paradigm: Paradigm,
  ): 'conjugation' | 'declension' | 'generic' {
    const pos = paradigm.partOfSpeech.toLowerCase();

    if (pos === 'verb' || pos === 'aux') {
      return 'conjugation';
    } else if (this.isRegularNoun(pos, paradigm)) {
      return 'declension';
    } else {
      return 'generic';
    }
  }

  // i.e. if this is a noun with exactly four cases (nominative, genitive, dative, accusative)
  private static isRegularNoun(pos: string, paradigm: Paradigm) {
    return (pos === 'noun' || pos === 'adj') && !this.hasAdditionalCases(paradigm);
  }

  // Could be derived more elegantly, but the number of cases actually does depend on the language
  private static hasAdditionalCases(paradigm: Paradigm): boolean {
    return paradigm.languageCode.toLowerCase() === 'ru';
  }
}
