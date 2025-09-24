# Serverless Morphology

The Serverless Morphology service is a AWS Lambda Function behind an API Gateway that provides a
REST API for morphological analysis of text. It re-uses the code of the `morphology` image,
with a few key differences:

| Feature     | WebServer                         | Serverless       |
|-------------|-----------------------------------|------------------|
| Entrypoint  | FastAPI Server                    | Lambda Handler   |
| spaCy Model | Downloaded on application startup | Built into image |

### Differences

The difference between the way to deal with spaCy models lies in that AWS Lambda, the chosen runtime
for the Serverless implementation, has a read-only filesystem except for the `/tmp` directory.
Unfortunately, downloading models to a particular directory is not supported, so the model must
be included in the image.

Therefore, a `matrix` deployment is created in the GitHub Actions workflow that builds these images
and tags them as follows:

`246770851643.dkr.ecr.eu-central-1.amazonaws.com/morphology:${VERSION}-${LANGUAGE}`, for example
`246770851643.dkr.ecr.eu-central-1.amazonaws.com/morphology:0.3.0-ru`.

### Performance

In order to avoid cold-starts, an Eventbridge rule is created that fires every ten minutes and
ensures the container stays alive, and the model stays in memory.