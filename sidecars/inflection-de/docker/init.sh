#!/bin/sh

poetry run spacy download de_core_web_sm

python3 -m uvicorn inflection.main:app --host=0.0.0.0 --port=8000