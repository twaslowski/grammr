from pydantic import BaseModel


class Note(BaseModel):
    id: str
    question: str
    answer: str
