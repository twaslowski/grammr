from morphology import lambda_handler


def test_should_return_keep_warm_response():
    event = {"body": '{"keep-warm": true}'}
    response = lambda_handler.handler(event, None)
    assert response["statusCode"] == 200
    assert response["body"] == '{"keep-warm": "success"}'
