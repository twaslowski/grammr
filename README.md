# grammr

`grammr` is a language learning tool that aims to make understanding the grammar of a variety
of languages easier.

- 🌍 Translate texts
- 📖 Get translations for every word in the text in-context
- 🔎 Get a grammatical analysis for every word in the text
- 💡 Conjugate and decline words
- 📚 Create flashcards to memorize words and phrases
- 🌈 Many more features coming! Check out the [Roadmap](#Features) for more information.

### 👉🏼 [Try it out](https://grammr.app).

---

[![Core](https://github.com/twaslowski/grammr/actions/workflows/deploy_core.yml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_core.yml)

[![Morphology](https://github.com/twaslowski/grammr/actions/workflows/deploy_morphology_serverless.yaml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_serverless.yaml)

[![Generic inflections](https://github.com/twaslowski/grammr/actions/workflows/deploy_multi_inflection.yaml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_morphology.yaml)

[![Russian inflections](https://github.com/twaslowski/grammr/actions/workflows/deploy_inflection_ru.yaml/badge.svg)](https://github.com/twaslowski/grammr/actions/workflows/deploy_inflection_ru.yaml)

![Vercel Deploy](https://deploy-badge.vercel.app/vercel/grammr)

---

## In this doc

- [About](#About)
- [Features](#Features)
- [Related projects](#Related-projects)
- [Running](#Running)
- [Developing](#Developing)
- [Domain Language](#glossary)

## About

While there are too many great language learning apps to count, standardized grammatical references
for languages can be hard to come by. It is possible to find dictionaries and inflection tables for
different languages by googling; however, there is no standardized tool so far that does this
for a variety of languages.

The aim of this project is to create a unified API to provide several tools for language learners
across multiple languages:

- Translations of texts, including the literal translations of individual words
- Morphological morphology of words, including their part-of-speech tags and features
- Inflection (conjugation and declension) of words
- Anki Flashcard export to make learning easier

This project, does not take a didactic approach to learning languages, and should
not be compared to an app like Duolingo; it rather aims to be a comprehensive, open reference
tool that can be arbitrarily extended for different languages.

However, it **does** offer a chat, to make it easier to learn languages in a conversational
manner. While chatting, you can perform analyses of phrases which can then be stored for later
study.

### Features

- Chat with a language model
- Translate sentences across arbitrary languages
- Get literal translations for words
- Text-to-speech for translations
- Inflection tables (supported: 🇷🇺🇮🇹🇫🇷🇪🇸🇵🇹)
- Creation and sync of Flashcards for Anki

or may not have equivalents in different languages)

⏰ More will be added as the project progresses. Check back later!

## Related projects

Listed here are projects that I am either using or would consider integrating into this project.

- [spaCy](https://spacy.io/): Morphological morphology for a variety of languages.
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

### Running locally 

I tried to make running the project yourself as straightforward as possible. What you'll need:

- An OpenAI API key. You can get one by signing up [here](https://platform.openai.com/signup).
- A Clerk API key for user management. Ideally, different identity providers should be supported
to make this more flexible, but for now, due to its user-friendliness, Clerk is the only supported
identity provider. You can sign up [here](https://clerk.dev/).

`./lifecycle/run.sh`, gives you a very minimal setup to run the project locally. It starts:
- A Postgres database, which is used to store user data and translations.
- Containers for providing morphological analysis for Russian and German.
- Containers for providing inflections for Russian
- A anki export server, which is used to package user flashcards into APKG files
- A mockserver mocking the OpenAI Responses API

### Kubernetes

Given the complexity of this project, Kubernetes is the recommended way to run it due to the
sheer amount of services that need to be run. This project comes with multiple Helm charts
that you can use. This will require a running Kubernetes cluster (you could try using
[Minikube](https://minikube.sigs.k8s.io/docs/) or [k3s](https://k3s.io/)) and
[Helm](https://helm.sh/) to be installed.

The Helm charts exist over at [grammr-charts](https://github.com/twaslowski/grammr-charts/).

Installation:

```shell
helm repo add grammr https://twaslowski.github.io/grammr-charts

# create the database first
helm install --namespace grammr --create-namespace \
  postgres oci://registry-1.docker.io/bitnamicharts/postgresql
  
# core
helm install --namespace grammr \
    --set openai_api_key="$OPENAI_API_KEY" \
    grammr-core grammr/grammr-core
```

For running the Sidecars, refer to the `charts/` directory and the `environments/` directory
to see the supplied values.

## Developing

PRs are welcome! I'm happy to help you get started with the project, so feel free to reach out.

You can run unit tests with `./lifecycle/unit-test.sh` and integration tests with `./lifecycle/integration-test.sh`.
Alternatively, run both with `./lifecycle/qa.sh`. This will also ensure the most accurate test coverage
report is generated, which you can access at `target/site/jacoco/index.html`.

## Glossary

One thing that I attempted to do with this project is develop a clear domain language
based on [Universal Dependencies](https://universaldependencies.org/).

Unfortunately, this has turned out to be difficult, and currently terms are somewhat inconsistent
here and there. Note, also, that I am not a linguist and have no relevant training. 

### Lemmas, Lexemes and Paradigms

- **Inflections** are a key component of this project. Inflections are, as per Wikipedia:
> [...] a process of word formation in which a word is modified to express different grammatical
> categories such as tense, case, voice, aspect, person, number, gender, mood, animacy, and definiteness.

One achievement of this project is to provide a unified inflections API across multiple languages
by encapsulating a variety of usually Python-based libraries into a single API.

You will note that a single `Inflection` consists of the root form of the word, its features,
and the inflected form of the word. Multiple inflections form a `Paradigm`, which is
"_the complete set of related word forms associated with a given lexeme_"
[[source](https://en.wikipedia.org/wiki/Morphology_(linguistics)#Paradigms_and_morphosyntax)].

Unfortunately, I mixed up the terms `lexeme` and `lemma` a fair bit. To be precise: A `lemma`
is a base concept in spaCy and refers to the canonical form of a word, while a `lexeme`
is a unit of meaning. A `Paradigm` belongs to a `lexeme`, not a `lemma`.
This is something I got wrong and may have to fix in the future.

### Tokens

- `Token` refers to a singular word of a phrase, that contains
  - a `source_text`
  - a `lemma`
  - a `pos` (part of speech, such as `NOUN`). [Reference](https://universaldependencies.org/u/pos/index.html).
  _- a `feature_set` (a set of features, such as `NUMBER=PLURAL` and `CASE=GEN`). [Reference](https://universaldependencies.org/u/feat/index.html).
  - an optional `ancestor` (a reference to another `Token` in the phrase that it relates to)._

- `Phrase` refers to a collection of `Tokens` that form a sentence.

- `Tokens` are aggregated through the process of literally translating and grammatically analyzing
phrases. These processes return Sets of `TokenTranslations` and `TokenMorphology` respectively,
which are coalesced into `Tokens` that ultimately make up a `Phrase`.
Therefore, a `Analysis` consists of a set of `Tokens` holding their literal translation
and grammatical morphology, as well as the semantic translation of the phrase.

### Part of Speech & Features

I've decided to use the [Universal Dependencies](https://universaldependencies.org/) as reference
for my part-of-speech and feature definitions as well as the domain language defined above.

- `Part of Speech` (POS) is a grammatical category of words that have similar grammatical properties.
Read more: https://universaldependencies.org/u/pos/index.html

- `Features` are morphological and syntactic properties of words. They are used to distinguish
between different grammatical categories of words. Read more: https://universaldependencies.org/u/feat/index.html
