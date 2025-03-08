from pydantic import BaseModel, Field


class AnalysisRequest(BaseModel):
    phrase: str
    request_id: str | None = Field(..., alias="requestId")
