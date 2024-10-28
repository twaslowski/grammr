from pydantic import BaseModel


class AnalysisRequest(BaseModel):
  phrase: str
  language: str
  request_id: str
