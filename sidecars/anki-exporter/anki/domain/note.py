from pydantic import BaseModel


class Note(BaseModel):
    id: int
    question: str
    answer: str
