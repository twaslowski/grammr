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


def check_keep_warm(event: dict[str, str]) -> dict | None:
    body = json.loads(event.get("body", "{}"))
    if body.get("keep-warm") is not None:
        return ok({"keep-warm": "success"})
    return None
