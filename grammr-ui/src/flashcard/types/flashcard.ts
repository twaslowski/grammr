export interface Flashcard {
  question: string;
  answer: string;
  id: string;
  status:
    | 'CREATED'
    | 'UPDATED'
    | 'MARKED_FOR_DELETION'
    | 'CREATION_INITIATED'
    | 'UPDATE_INITIATED'
    | 'DELETION_INITIATED'
    | 'CREATION_FAILED'
    | 'UPDATE_FAILED'
    | 'DELETION_FAILED';
  createdTimestamp: string;
  updatedTimestamp: string;
}
