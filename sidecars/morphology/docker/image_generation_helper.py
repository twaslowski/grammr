import yaml
import json

with open("config.yaml", "r") as f:
    config = yaml.safe_load(f)

    matrix = []
    for language in config.get("languages", []):
        matrix.append({"code": language.get("code"), "model": language.get("model")})

    print(json.dumps(json.dumps(matrix)))
