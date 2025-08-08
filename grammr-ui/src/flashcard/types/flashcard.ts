export interface Flashcard {
  question: string;
  answer: string;
  id: string;
  status: 'CREATED' | 'UPDATED' | 'MARKED_FOR_DELETION' | 'SYNCED';
  createdTimestamp: string;
  updatedTimestamp: string;
}
