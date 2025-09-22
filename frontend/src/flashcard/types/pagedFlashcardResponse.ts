import { Flashcard } from './flashcard';

export interface PagedFlashcardResponse {
  content: Flashcard[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
}
