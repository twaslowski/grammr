from anki.domain.note import Note
from pydantic import BaseModel, Field


class Deck(BaseModel):
    id: int = Field(..., alias="deckId")
    name: str
    notes: list[Note]
