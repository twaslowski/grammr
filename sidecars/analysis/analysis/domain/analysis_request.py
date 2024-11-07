from pydantic import BaseModel


class AnalysisRequest(BaseModel):
    phrase: str
    request_id: str
