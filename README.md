[![Russian inflections](https://github.com/twaslowski/grammr/actions/workflows/deploy_inflection_ru.yaml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_inflection_ru.yaml)
[![Russian inflections](https://github.com/twaslowski/grammr/actions/workflows/deploy_morphology.yaml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_multi_inflection.yaml)
[![Russian inflections](https://github.com/twaslowski/grammr/actions/workflows/deploy_multi_inflection.yaml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_morphology.yaml)
[![Russian inflections](https://github.com/twaslowski/grammr/actions/workflows/deploy_core.yml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_core.yml)

# grammr

`grammr` is a language learning tool that aims to make understanding the grammar of a variety
of primarily Indo-European languages easier. The frontend is available on
[Vercel](https://grmmr.vercel.app/).

## About

While there are too many great language learning apps to count, standardized grammatical references
for languages can be hard to come by. It is possible to find dictionaries and inflection tables for
different languages by googling; however, there is no standardized tool so far that does this
for a variety of languages.

The aim of this project is to create a unified API to provide several tools for language learners
across multiple languages:

- Translations of texts, including the literal translations of individual words
- Morphological analysis of words, including their part-of-speech tags and features
- Inflection (conjugation and declension) of words
- Anki Flashcard export to make learning easier

I have more ideas for this project, which you can find in the [Features](#Features) and
[Roadmap](#Roadmap) sections.

This project, therefore, does not take a didactic approach to learning languages, and should
not be compared to an app like Duolingo; it rather aims to be a comprehensive, open reference
tool that can be arbitrarily extended for different languages.

## Features

- Translation of texts.
- Literal translations for each word in a text.
- Morphological analysis of each word in a text.
- Creation of inflections for words.

The morphological analysis is done using [spaCy](https://spacy.io/), and contains the following information for each word:

- The part of speech (POS) of a word.
- The features of a word, including the Case, Number, Gender, Tense, Person, depending on word type,
as well as its lemma, its basic form.
For more information on this, see the [Universal Dependencies](https://universaldependencies.org/u/feat/index.html).

I'm looking to add more features in the near future. These may include:

- [x] The ability to arbitrarily add languages (solved via [Configmaps](https://github.com/twaslowski/grammr/commit/889284f0#diff-782f304121c40d11b8bcd8db123db62a7a8192bbcc8b8098cf07064774cc7c24))
- [x] A solid frontend, in addition to the Telegram bot currently available
- [x] Inflection tables (for Russian only right now)
- [x] Anki flash card export
- [ ] Instead of solely relying on LLMs for translation, maybe fetch translations via Wiktionary
or a related project and use them as a reference for the LLMs. For one, all meanings of a word
could be covered; also this would protect against wonky LLM responses.

## Technical Roadmap

Beyond the features mentioned above, I'm also looking to make the technology stack more robust.
I believe that this application should _really_ be able to scale to complexity, so I'm investing
a lot of time and energy into keeping the architecture as clean as possible.

Features and improvements will include:

- [x] A proper CI/CD pipeline, including automated deployment (halfway there)
- [x] Adding several more languages, which means creating additional sidecars for morphological
analyis.
- [x] Building a frontend. Possibly web-app, possibly cross-platform apps.
- [ ] Benchmarking against quality regression of prompts
- [x] ~~Extract~~ Delete the Telegram Bot entirely from the core service. It was useful for prototyping, but
should not be tied to the main application.

## Related projects

Listed here are projects that I am either using or would consider integrating into this project.

- [spaCy](https://spacy.io/): Morphological analysis for a variety of languages.
- [pymorphy3](https://github.com/no-plagiarism/pymorphy3): Fork of the currently unmaintained [pymorphy2](https://github.com/pymorphy2/pymorphy2).
Provides inflections for Russian.

Interesting related projects:

- [textile](https://github.com/SalahEddineGhamri/textile): Inspiration for the UI
- [mathigatti/spanish_inflections](https://github.com/mathigatti/spanish_inflections?tab=readme-ov-file): Inflections for Spanish
- [DuyguA/DEMorphy](https://github.com/DuyguA/DEMorphy): Inflections for German. Technically, this library
only provides _morphological analysis_, but it does use a comprehensive lexicon under the hood,
so creating inflections should be possible.
- [TimoBechtel/satzbau](https://github.com/TimoBechtel/satzbau): Creation of natural language German texts, including declension and conjugation.
- [verbecc](https://github.com/bretttolbert/verbecc): Verb conjugation for a variety of roman languages.

## Running

I tried to make running the project yourself as straightforward as possible. What you'll need:

- A Telegram bot token. You can get one by talking to the [BotFather](https://t.me/botfather).
- An OpenAI API key. You can get one by signing up [here](https://platform.openai.com/signup).

Given those, you can run the project with a local configuration or by deploying the packaged
Helm chart. My recommendation is using an `.envrc` file so you always have your environment variables
handy, but do whatever works best for you.

By running `./scripts/run.sh`, a Postgres container and the Python sidecar running spaCy will be
launched. The Spring application will be run via `mvn spring-boot:run`.

If you choose to run `./scripts/deploy.sh`, a Helm chart will be created and deployed to your
Kubernetes cluster. This will require a running Kubernetes cluster (you could try using
[Minikube](https://minikube.sigs.k8s.io/docs/) or [k3s](https://k3s.io/)) and
[Helm](https://helm.sh/) to be installed. You should set a DATASOURCE_PASSWORD environment variable,
which will be used to create a secret in the cluster for authentication with Postgres.

## Developing

PRs are welcome! I'm happy to help you get started with the project, so feel free to reach out.

You can run unit tests with `./scripts/unit-test.sh` and integration tests with `./scripts/integration-test.sh`.
Alternatively, run both with `./scripts/qa.sh`. This will also ensure the most accurate test coverage
report is generated, which you can access at `target/site/jacoco/index.html`.

## Domain Language

Different projects use different terms to describe similar concepts. For instance, while
[pymorphy2](https://github.com/pymorphy2/pymorphy2) refers to the root form of a word as the `lexeme`,
[spaCy](https://spacy.io/) refers to it as the `lemma`. I've tried to standardize the terms used in
a unified domain language that is to be used across the application and in the APIs it exposes.

The design is a work in progress, but I do think it offers some genuine value.

- `Token` refers to a singular word of a phrase, that contains
  - a `source_text`
  - a `lemma`
  - a `pos` (part of speech, such as `NOUN`). [Reference](https://universaldependencies.org/u/pos/index.html).
  _- a `feature_set` (a set of features, such as `NUMBER=PLURAL` and `CASE=GEN`). [Reference](https://universaldependencies.org/u/feat/index.html).
  - an optional `ancestor` (a reference to another `Token` in the phrase that it relates to)._

- `Phrase` refers to a collection of `Tokens` that form a sentence.

- A `SemanticTranslation` is a `Phrase` that is a translation of another `Phrase`. The _meaning_
of the original phrase is preserved as well as possible in the translation.
It consists of a `source_phrase` and a `target_phrase`.

- A `LiteralTranslation` is a direct, literal translation of a phrase. This can help users better
understand how phrases are constructed. It consists of the `source phrase` and a collection of
`TokenTranslations`, which are a key-value pair of tokens from the source phrase and their
directly translated counterparts. For example, "Ich bin ein Student" would contain four translated
tokens: `(Ich, I)`, `(bin, am)`, `(ein, a)`, `(Student, student)`.

- `Tokens` are aggregated through the process of literally translating and grammatically analyzing
phrases. These processes return Sets of `TokenTranslations` and `TokenMorphology` respectively,
which are coalesced into `Tokens` that ultimately make up a `Phrase`.
Therefore, a `Analysis` consists of a set of `Tokens` holding their literal translation
and grammatical analysis, as well as the semantic translation of the phrase.

### Part of Speech & Features

I've decided to use the [Universal Dependencies](https://universaldependencies.org/) as reference
for my part-of-speech and feature definitions as well as the domain language defined above.

- `Part of Speech` (POS) is a grammatical category of words that have similar grammatical properties.
Read more: https://universaldependencies.org/u/pos/index.html

- `Features` are morphological and syntactic properties of words. They are used to distinguish
between different grammatical categories of words. Read more: https://universaldependencies.org/u/feat/index.html
