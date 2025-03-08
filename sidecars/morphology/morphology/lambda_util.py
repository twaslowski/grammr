import json


def ok(res: dict | list) -> dict:
    return {
        "statusCode": 200,
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps(res),
    }


def fail(status: int) -> dict:
    return {
        "statusCode": status,
        "headers": {"Content-Type": "application/json"},
        "body": json.dumps({}),
    }


def check_pre_warm(event: dict[str, str]) -> dict | None:
    # todo refactor this
    # should be two separate functions is_pre_warm and return_pre_warm with a static response value
    body = json.loads(event.get("body", "{}"))
    if body.get("pre_warm") is not None:
        return ok({"pre-warmed": "true"})
    return None
