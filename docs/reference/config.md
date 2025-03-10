# The config.yaml file

`config.yaml` defines, largely, how `grammr` deploys and behaves. It consists of several properties,
some of which are required to perform deployments; some of which are required for the application
to perform its calls.

Its primary advantage is that it keeps the information synchronised in a single place.
However, its primary _disadvantage_ is that different consumers of the file require different
properties, which can lead to confusion and bloat.

The file, in its current state, looks like this:

```yaml
languages:
  - code: ru
    model: ru_core_news_sm
    morphology:
      enabled: true
      uri: "http://localhost:8010/morphological-analysis"
    inflection:
      enabled: true
      uri: "http://localhost:8020/inflect"
  - code: de
    model: de_core_news_sm
    morphology:
      enabled: true
      uri: "http://localhost:8010/morphological-analysis"
    inflections:
      enabled: false
```

Its consumers are:

- All Helm charts. It is always provided as a values.yaml for different purposes:
  - `grammr`: Injected into the `grammr` application as a Configmap
  - `morphology`: Required to spawn Pods with the morphology Docker image
  - `multi-inflection`: Required to spawn Pods with the inflection Docker image
- The Terraform module. It is required to provision the necessary infrastructure.
- Github Actions. It is required to build the Docker images and deploy them to ECR.

## Specification

This section outlines which consumers require which properties.

### grammr

Essentially, grammr only requires information on which services (morphology, inflection) are available
to it for a given language, and where to retrieve those resources.

- `languages`: Required to spawn the `grammr` application. It is a list of languages, each with the following properties:
  - `code`: The language code, e.g. `ru` for Russian.
  - `morphology`
    - `enabled`: Whether morphological analysis is available
    - `uri`: The URI of the morphology service.
  - `inflection`
    - `enabled`: Whether inflections are available
    - `uri`: The URI of the inflection service.

### morphology

**Helm**

The morphology Helm chart requires only a mapping of language code to model name.

- `languages`:
  - `code`: The language code, e.g. `ru` for Russian.
  - `model`: The model to use for the language.

**Serverless**

- `languages`:
  - `code`: The language code, e.g. `ru` for Russian.
  - `model`: The model to use for the language.

### multi-inflection

The multi-inflection service only requires an array of language codes.

### GitHub Actions

- `languages`:
  - `code`: The language code, e.g. `ru` for Russian.
  - `model`: The model to use for the language.

### Terraform

- `languages`:
  - `code`: The language code, e.g. `ru` for Russian.
  - `model`: The model to use for the language.
