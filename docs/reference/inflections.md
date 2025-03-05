# The Inflections API

Several microservices exist to create declensions and conjugations for words in various languages.
These services are powered by different libraries and are deployed as separate services.
They are unified by a shared API and shared behaviour, as to not expose this fact to the core
service. This document outlines their API.

## Request structure

Inflection services usually run as FastAPI servers and listen to POST requests on the `/inflect`
endpoint. The request body should be a JSON object with the following fields:

- `word`: The root word to inflect.
- `part_of_speech`: The part of speech of the word. This matters because different services may
only be able to deal with specific word types (e.g. only Verbs, but not Nouns).

An example request object looks like this:

```json
{
    "word": "essere",
    "part_of_speech": "verb"
}
```

## Response structure

The response object should be an `Inflections` object, which is a JSON object containing a list
of inflected words as well as some meta-information. It looks as follows:

```json
{
	"partOfSpeech": "VERB",
	"lemma": "manger",
	"inflections": [
		{
			"lemma": "manger",
			"inflected": "je mange",
			"features": [
				{
					"type": "PERSON",
					"value": "FIRST"
				},
				{
					"type": "NUMBER",
					"value": "SING"
				}
			]
		},
		{
			"lemma": "manger",
			"inflected": "tu manges",
			"features": [
				{
					"type": "PERSON",
					"value": "SECOND"
				},
				{
					"type": "NUMBER",
					"value": "SING"
				}
			]
		},
		...
	]
}
```