from analysis.domain.analysis_request import AnalysisRequest


def test_should_deserialize_valid_json():
    json = """
  {
    "requestId": "someId",
    "phrase": "somephrase"
  }
  """
    parsed = AnalysisRequest.parse_raw(json)
    assert parsed.request_id == "someId"
    assert parsed.phrase == "somephrase"
