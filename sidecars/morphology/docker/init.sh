#!/bin/sh

echo "Installing model ${SPACY_MODEL}"
python3 -m spacy download "${SPACY_MODEL}"

echo "Starting FastAPI server"
python3 -m uvicorn morphology.main:app --host=0.0.0.0 --port=8000