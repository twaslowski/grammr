from pydantic import BaseModel, Field


class InflectionRequest(BaseModel):
    lemma: str
    part_of_speech: str = Field(..., alias="partOfSpeech")
