from pydantic import BaseModel, Field


class AnalysisRequest(BaseModel):
    phrase: str | None
    request_id: str | None = Field(..., alias="requestId")
