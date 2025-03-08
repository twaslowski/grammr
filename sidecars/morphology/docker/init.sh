#!/bin/sh

echo "Installing model ${SPACY_MODEL}"


echo "Starting FastAPI server"
python3 -m uvicorn analysis.main:app --host=0.0.0.0 --port=8000