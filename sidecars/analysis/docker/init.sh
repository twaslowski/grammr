#!/bin/sh

if [ -z "$PREBUILD_WITH_MODEL" ]; then
  python3 download_models.py
fi
python3 -m uvicorn analysis.main:app --host=0.0.0.0 --port=8000