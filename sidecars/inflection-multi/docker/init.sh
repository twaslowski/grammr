#!/bin/sh

echo "Starting FastAPI server"
python3 -m uvicorn inflection.main:app --host=0.0.0.0 --port=8000