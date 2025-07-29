export interface Flashcard {
  question: string;
  answer: string;
  id: string;
  status: 'CREATED' | 'UPDATED' | 'EXPORT_INITIATED' | 'EXPORT_COMPLETED' | 'EXPORT_FAILED';
  createdTimestamp: string;
  updatedTimestamp: string;
}
