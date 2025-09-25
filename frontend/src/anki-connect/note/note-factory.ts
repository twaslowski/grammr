import { Flashcard } from '@/flashcard/types/flashcard';
import { MODEL_NAMES } from '@/anki-connect/model/model-names';
import { AnyNote, BasicNote, ConjugationNote, DeclensionNote, InflectionNote } from './type';
import { extractVerbForms } from './conjugation-utils';
import { extractCases } from '@/anki-connect/note/declension-utils';

export class NoteFactory {
  static createFromFlashcard(flashcard: Flashcard, deckName: string): AnyNote {
    if (flashcard.paradigm) {
      const noteType = this.getInflectionNoteType(flashcard.paradigm.partOfSpeech);

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
        front: flashcard.question,
        back: flashcard.answer,
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
        front: flashcard.question,
        back: flashcard.answer,
        lemma: flashcard.paradigm.lemma,
        translation: flashcard.answer,
        table: '',
      },
      modelName: MODEL_NAMES.INFLECTION_GENERIC,
      deckName: deckName,
    };
  }

  private static getInflectionNoteType(
    partOfSpeech: string,
  ): 'conjugation' | 'declension' | 'generic' {
    const pos = partOfSpeech.toLowerCase();

    if (pos === 'verb' || pos === 'aux') {
      return 'conjugation';
    } else if (pos === 'noun' || pos === 'adj') {
      return 'declension';
    } else {
      return 'generic';
    }
  }
}
