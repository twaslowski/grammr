from pydantic import BaseModel, Field


class AnalysisRequest(BaseModel):
    phrase: str
    language_code: str = Field(..., alias="languageCode")
    request_id: str | None = Field(..., alias="requestId")
