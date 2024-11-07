#!/bin/sh

if [ -z "${SPACY_MODEL}" ]; then
    echo "SPACY_MODEL is not set. Exiting."
    exit 1
fi

python3 -m spacy install "${SPACY_MODEL}"
python3 -m uvicorn analysis.main:app --host 0.0.0.0 --port 8000