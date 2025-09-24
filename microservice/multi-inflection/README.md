# inflections-multi

This microservice enables inflection of words for French, Italian, Spanish and Italian.
It is powered by [verbecc](https://github.com/bretttolbert/verbecc).

## Deployment

It is important to keep the pyproject.toml `version` in sync with the Chart.yaml `appVersion`.
The `version` generates a Docker image tag, which ends up being used in the Chart by default,
although it can be overridden if you supply `Values.image.tag`.

## Usage

You can run the application locally or via Docker:

```shell
python3 -m uvicorn inflection.main:app --host=0.0.0.0 --port=8000
```

```shell
docker run -p 8000:8000 -d --name inflection-ru tobiaswaslowski/inflection-ru
```

You can then use the service to get inflections for Russian words:

```shell
curl -X 'POST' \
  'http://localhost:8000/inflect' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "word": "essere",
  "part_of_speech": "VERB"
}'
```
Note that since only conjugation is supported, the `part_of_speech` does not actually serve any purpose;
however, it is required by the API.