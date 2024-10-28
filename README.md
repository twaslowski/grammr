# grammr

This is a clean rewrite of [lingolift](https://github.com/twaslowski/lingolift-core).

## Domain Language

- `Token` refers to a singular word of a phrase, that contains
  - a `source_text`
  - a `lemma`
  - a `pos` (part of speech, such as `NOUN`). [Reference](https://universaldependencies.org/u/pos/index.html).
  - a `feature_set` (a set of features, such as `NUMBER=PLURAL` and `CASE=GEN`). [Reference](https://universaldependencies.org/u/feat/index.html).
  - an optional `ancestor` (a reference to another `Token` in the phrase that it relates to).

- `Phrase` refers to a collection of `Tokens` that form a sentence.
- `Translation` is a `Phrase` that is a translation of another `Phrase`.
It consists of a `source_phrase` and a `target_phrase`.
- A `Literal Translation` is a direct, literal translation of a word. It can be combined
with a `Token` to enhance understanding of how a `Phrase` in the source language is constructed.
It consists of:
  - 