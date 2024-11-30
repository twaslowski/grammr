#!/bin/sh

python3 download_models.py
python3 -m uvicorn analysis.main:app --host=0.0.0.0 --port=8000