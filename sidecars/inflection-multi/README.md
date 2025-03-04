# inflections-multi

This microservice enables inflection of words for French, Italian, Spanish and Italian.
It is powered by [verbecc](https://github.com/bretttolbert/verbecc).

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
  "word": "уметь",
  "part_of_speech": "VERB"
}'
```

This will automatically generate all different inflections for the word "уметь".
Supplying the part_of_speech tag is important in order to determine whether to conjugate or
decline the word.