# multi-inflection

This chart utilizes a Docker image built on top of the `verbecc` Python library to provide
conjugations for Italian, French, Spanish, Portuguese and Romanian.

It will spawn Pods/Services listening on port 8000 for each language supplied in the `languageCodes`
array. Allowed values for the `languageCodes` array are: `it`, `fr`, `es`, `pt`, `ro`.