#!/bin/sh

echo "Installing model ${SPACY_MODEL}"
python3 -m spacy download "${SPACY_MODEL}"

echo "Starting morphological analysis service"
python3 analysis/event_receiver.py